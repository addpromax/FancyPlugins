package com.fancyinnovations.fancydialogs.dialog;

import com.fancyinnovations.fancydialogs.FancyDialogsPlugin;
import com.fancyinnovations.fancydialogs.api.Dialog;
import com.fancyinnovations.fancydialogs.api.data.DialogBodyData;
import com.fancyinnovations.fancydialogs.api.data.DialogButton;
import com.fancyinnovations.fancydialogs.api.data.DialogData;
import com.fancyinnovations.fancydialogs.api.data.condition.ConditionEvaluator;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogCheckbox;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogInput;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogSelect;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogTextField;
import de.oliver.fancysitula.api.dialogs.FS_CommonDialogData;
import de.oliver.fancysitula.api.dialogs.FS_Dialog;
import de.oliver.fancysitula.api.dialogs.FS_DialogAction;
import de.oliver.fancysitula.api.dialogs.actions.FS_CommonButtonData;
import de.oliver.fancysitula.api.dialogs.actions.FS_DialogActionButton;
import de.oliver.fancysitula.api.dialogs.actions.FS_DialogActionButtonAction;
import de.oliver.fancysitula.api.dialogs.actions.FS_DialogCopyToClipboardAction;
import de.oliver.fancysitula.api.dialogs.actions.FS_DialogCustomAction;
import de.oliver.fancysitula.api.dialogs.body.FS_DialogBody;
import de.oliver.fancysitula.api.dialogs.body.FS_DialogTextBody;
import de.oliver.fancysitula.api.dialogs.inputs.*;
import de.oliver.fancysitula.api.dialogs.types.FS_MultiActionDialog;
import de.oliver.fancysitula.api.dialogs.types.FS_NoticeDialog;
import de.oliver.fancysitula.api.entities.FS_RealPlayer;
import de.oliver.fancysitula.factories.FancySitula;
import org.bukkit.entity.Player;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.chatcolorhandler.parsers.Parser;
import org.lushplugins.chatcolorhandler.parsers.ParserTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DialogImpl extends Dialog {

    public DialogImpl(String id, DialogData data) {
        super(id, data);
    }

    /**
     * 获取不包含 MiniMessage 翻译键解析器的 Parser 列表
     * 这样可以保留 <lang:xxx> 标签，让客户端根据自己的语言设置来显示
     */
    private List<Parser> getParsersWithoutTranslatable() {
        return ChatColorHandler.parsers().getRegisteredParsers().stream()
                .filter(parser -> !(parser.getClass().getSimpleName().equals("MiniMessagePlaceholderParser")))
                .collect(Collectors.toList());
    }

    /**
     * 将文本转换为 MiniMessage 格式，保留翻译键
     * 直接使用 parseString 方法，指定输出类型为 MINI_MESSAGE
     */
    private String translateToMiniMessage(String text, Player player, List<Parser> parsers) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        // 直接使用 parseString 方法，指定输出类型为 MINI_MESSAGE
        // 这样会解析占位符，但保持 MiniMessage 格式，不会转换为 § 代码
        return ChatColorHandler.parsers().parseString(text, Parser.OutputType.MINI_MESSAGE, player, parsers);
    }

    private FS_Dialog buildForPlayer(Player player) {
        List<Parser> parsers = getParsersWithoutTranslatable();
        
        List<FS_DialogBody> body = new ArrayList<>();
        for (DialogBodyData bodyData : data.body()) {

            int textWidth = (bodyData.width() != null && bodyData.width() > 0)
                    ? bodyData.width()
                    : 200;

            FS_DialogTextBody fsDialogTextBody = new FS_DialogTextBody(
                    translateToMiniMessage(bodyData.text(), player, parsers),
                    textWidth
            );
            body.add(fsDialogTextBody);
        }

        List<FS_DialogInput> inputs = new ArrayList<>();
        if (data.inputs() != null) {
            for (DialogInput input : data.inputs().all()) {
                // Check visibility conditions
                if (!ConditionEvaluator.evaluateAll(input.getVisibilityConditions(), player)) {
                    continue; // Skip this input if conditions are not met
                }
                
                FS_DialogInputControl control = null;
                if (input instanceof DialogTextField textField) {
                    control = new FS_DialogTextInput(
                            200, // default width
                            translateToMiniMessage(textField.getLabel(), player, parsers),
                            !textField.getLabel().isEmpty(),
                            translateToMiniMessage(textField.getPlaceholder(), player, parsers),
                            textField.getMaxLength(),
                            textField.getMaxLines() > 1 ?
                                    new FS_DialogTextInput.MultilineOptions(textField.getMaxLines(), null) :
                                    null
                    );
                } else if (input instanceof DialogSelect select) {
                    List<FS_DialogSingleOptionInput.Entry> entries = new ArrayList<>();
                    for (DialogSelect.Entry entry : select.getOptions()) {
                        // Evaluate selected conditions to determine if this option should be initially selected
                        boolean isInitiallySelected = entry.initial();
                        if (entry.selectedConditions() != null && !entry.selectedConditions().isEmpty()) {
                            // If selectedConditions are defined and met, this option should be selected
                            isInitiallySelected = ConditionEvaluator.evaluateAll(entry.selectedConditions(), player);
                        }
                        
                        entries.add(
                                new FS_DialogSingleOptionInput.Entry(
                                        translateToMiniMessage(entry.value(), player, parsers),
                                        translateToMiniMessage(entry.display(), player, parsers),
                                        isInitiallySelected
                                )
                        );
                    }
                    control = new FS_DialogSingleOptionInput(
                            200, // default width
                            entries,
                            translateToMiniMessage(select.getLabel(), player, parsers),
                            !select.getLabel().isEmpty()
                    );
                } else if (input instanceof DialogCheckbox checkbox) {
                    // Evaluate checked conditions to determine initial state
                    boolean initialChecked = checkbox.isInitial();
                    if (checkbox.getCheckedConditions() != null && !checkbox.getCheckedConditions().isEmpty()) {
                        initialChecked = ConditionEvaluator.evaluateAll(checkbox.getCheckedConditions(), player);
                    }
                    control = new FS_DialogBooleanInput(translateToMiniMessage(input.getLabel(), player, parsers), initialChecked, "true", "false");
                }

                if (control == null) {
                    throw new IllegalArgumentException("Unsupported input type: " + input.getClass().getSimpleName());
                }

                FS_DialogInput fsDialogInput = new FS_DialogInput(input.getKey(), control);
                inputs.add(fsDialogInput);
            }
        }

        List<FS_DialogActionButton> actions = new ArrayList<>();
        for (DialogButton button : data.buttons()) {
            // Check visibility conditions
            if (!ConditionEvaluator.evaluateAll(button.visibilityConditions(), player)) {
                continue; // Skip this button if conditions are not met
            }
            
            FS_DialogActionButtonAction buttonAction;

            if (button.actions().size() == 1 &&
                button.actions().get(0).name().equals("copy_to_clipboard")) {
                String text = translateToMiniMessage(
                        button.actions().get(0).data(),
                        player,
                        parsers
                );
                buttonAction = new FS_DialogCopyToClipboardAction(text);
            } else {
                buttonAction = new FS_DialogCustomAction(
                        "fancydialogs_dialog_action",
                        Map.of(
                                "dialog_id", id,
                                "button_id", button.id()
                        )
                );
            }

            FS_DialogActionButton fsDialogActionButton = new FS_DialogActionButton(
                    new FS_CommonButtonData(
                            translateToMiniMessage(button.label(), player, parsers),
                            translateToMiniMessage(button.tooltip(), player, parsers),
                            150 // default button width
                    ),
                    buttonAction
            );
            actions.add(fsDialogActionButton);
        }

        if (actions.isEmpty()) {
            return new FS_NoticeDialog(
                    new FS_CommonDialogData(
                            translateToMiniMessage(data.title(), player, parsers),
                            translateToMiniMessage(data.title(), player, parsers),
                            data.canCloseWithEscape(),
                            false,
                            FS_DialogAction.CLOSE,
                            body,
                            inputs
                    ),
                    new FS_DialogActionButton(
                            new FS_CommonButtonData(
                                    "Close",
                                    null,
                                    150 // default button width
                            ),
                            new FS_DialogCustomAction(
                                    "fancydialogs_dialog_action--none",
                                    Map.of())
                    )
            );
        }

        return new FS_MultiActionDialog(
                new FS_CommonDialogData(
                        translateToMiniMessage(data.title(), player, parsers),
                        translateToMiniMessage(data.title(), player, parsers),
                        data.canCloseWithEscape(),
                        false,
                        FS_DialogAction.CLOSE,
                        body,
                        inputs
                ),
                actions, // actions
                null,
                2
        );
    }

    @Override
    public void open(Player player) {
        FancySitula.PACKET_FACTORY
                .createShowDialogPacket(buildForPlayer(player))
                .send(new FS_RealPlayer(player));

        addViewer(player);

        FancyDialogsPlugin.get().getFancyLogger().debug("Opened dialog " + id + " for player " + player.getName());
    }

    @Override
    public void close(Player player) {
        FancySitula.PACKET_FACTORY
                .createClearDialogPacket()
                .send(new FS_RealPlayer(player));

        removeViewer(player);

        FancyDialogsPlugin.get().getFancyLogger().debug("Closed dialog " + id + " for player " + player.getName());
    }

    @Override
    public boolean isOpenedFor(UUID uuid) {
        if (uuid == null) {
            return false;
        }

        if (!viewers.containsKey(uuid)) {
            return false;
        }

        long openedAt = viewers.get(uuid);
        long now = System.currentTimeMillis();
        if (now - openedAt > FancyDialogsPlugin.get().getFancyDialogsConfig().getCloseTimeout()) {
            viewers.remove(uuid);
            return false;
        }

        return true;
    }

}
