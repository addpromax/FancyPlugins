package com.fancyinnovations.fancydialogs.listener;

import com.fancyinnovations.fancydialogs.FancyDialogsPlugin;
import com.fancyinnovations.fancydialogs.api.Dialog;
import com.fancyinnovations.fancydialogs.api.DialogAction;
import com.fancyinnovations.fancydialogs.api.data.DialogButton;
import com.fancyinnovations.fancydialogs.api.data.condition.ConditionEvaluator;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogInput;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogSelect;
import com.fancyinnovations.fancydialogs.api.events.DialogButtonClickedEvent;
import de.oliver.fancysitula.api.packets.FS_ServerboundCustomClickActionPacket;
import de.oliver.fancysitula.api.packets.FS_ServerboundPacket;
import de.oliver.fancysitula.api.utils.FS_PacketListener;
import de.oliver.fancysitula.factories.FancySitula;

import java.util.Map;

public class CustomClickActionPacketListener {

    private static CustomClickActionPacketListener INSTANCE;

    private final FS_PacketListener packetListener;

    public CustomClickActionPacketListener() {
        packetListener = FancySitula.PACKET_LISTENER_FACTORY.createPacketListener(FS_ServerboundPacket.Type.CUSTOM_CLICK_ACTION);
        packetListener.addListener(this::onPacketReceived);
    }

    public static CustomClickActionPacketListener get() {
        if (INSTANCE == null) {
            INSTANCE = new CustomClickActionPacketListener();
        }
        return INSTANCE;
    }

    private void onPacketReceived(FS_PacketListener.PacketReceivedEvent event) {
        if (!(event.packet() instanceof FS_ServerboundCustomClickActionPacket packet)) {
            return; // Ignore if the packet is not of the expected type
        }

        if (!packet.getId().namespace().equals("fancysitula") && !packet.getId().namespace().equals("fancydialogs_dialog_action")) {
            return; // Ignore packets not related to FancyDialogs
        }

        packet.getPayload().forEach((key, value) -> {
            FancyDialogsPlugin.get().getFancyLogger().debug("Click action data Key: " + key + " value: " + value.toString());
        });

        String dialogId = packet.getPayload().get("dialog_id");
        String buttonId = packet.getPayload().get("button_id");

        if (dialogId == null || buttonId == null) {
            return; // Missing necessary information
        }

        new DialogButtonClickedEvent(event.player(), dialogId, buttonId, packet.getPayload()).callEvent();

        if (dialogId.startsWith("confirmation_dialog_")) {
            return; // Ignore confirmation dialog actions, handled separately
        }

        Dialog dialog = FancyDialogsPlugin.get().getDialogRegistry().get(dialogId);
        if (dialog == null) {
            FancyDialogsPlugin.get().getFancyLogger().warn("Received action for unknown dialog: " + dialogId);
            return;
        }

        if (!dialog.isOpenedFor(event.player())) {
            FancyDialogsPlugin.get().getFancyLogger().warn("Received action for dialog: " + dialogId + " but it is not opened for player: " + event.player().getName());
            return;
        }

        DialogButton btn = dialog.getData().getButtonById(buttonId);
        if (btn == null) {
            FancyDialogsPlugin.get().getFancyLogger().warn("Received action for unknown button: " + buttonId + " in dialog: " + dialogId);
            return;
        }

        // Execute button actions
        for (DialogButton.DialogAction btnAction : btn.actions()) {
            DialogAction action = FancyDialogsPlugin.get().getActionRegistry().getAction(btnAction.name());
            if (action == null) {
                FancyDialogsPlugin.get().getFancyLogger().warn("Received action for unknown action: " + btnAction.name() + " in button: " + buttonId);
                continue;
            }

            String data = btnAction.data();
            for (Map.Entry<String, String> entry : packet.getPayload().entrySet()) {
                data = data.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            action.execute(event.player(), dialog, data);
        }
        
        // Execute select option actions (only if the selected option is different from current state)
        if (dialog.getData().inputs() != null) {
            for (DialogInput input : dialog.getData().inputs().all()) {
                if (!(input instanceof DialogSelect select)) {
                    continue;
                }
                
                String selectedValue = packet.getPayload().get(select.getKey());
                if (selectedValue == null) {
                    continue;
                }
                
                // Find the selected option
                for (DialogSelect.Entry option : select.getOptions()) {
                    if (!option.value().equals(selectedValue)) {
                        continue;
                    }
                    
                    // Check if this option is already the current state
                    if (option.selectedConditions() != null && !option.selectedConditions().isEmpty()) {
                        boolean isCurrentState = ConditionEvaluator.evaluateAll(option.selectedConditions(), event.player());
                        if (isCurrentState) {
                            // Skip execution - player selected the same option as current state
                            FancyDialogsPlugin.get().getFancyLogger().debug("Skipping action for select option '" + option.value() + "' - already in this state");
                            continue;
                        }
                    }
                    
                    // Execute option actions
                    if (option.actions() != null) {
                        for (DialogButton.DialogAction optionAction : option.actions()) {
                            DialogAction action = FancyDialogsPlugin.get().getActionRegistry().getAction(optionAction.name());
                            if (action == null) {
                                FancyDialogsPlugin.get().getFancyLogger().warn("Received action for unknown action: " + optionAction.name() + " in select option: " + option.value());
                                continue;
                            }
                            
                            String data = optionAction.data();
                            for (Map.Entry<String, String> entry : packet.getPayload().entrySet()) {
                                data = data.replace("{" + entry.getKey() + "}", entry.getValue());
                            }
                            
                            action.execute(event.player(), dialog, data);
                        }
                    }
                    
                    break; // Found the selected option, no need to continue
                }
            }
        }
    }

    public FS_PacketListener getPacketListener() {
        return packetListener;
    }
}
