package com.fancyinnovations.fancydialogs.registry;

import com.fancyinnovations.fancydialogs.FancyDialogsPlugin;
import com.fancyinnovations.fancydialogs.api.data.DialogBodyData;
import com.fancyinnovations.fancydialogs.api.data.DialogButton;
import com.fancyinnovations.fancydialogs.api.data.DialogData;
import com.fancyinnovations.fancydialogs.api.data.condition.ConditionType;
import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogCheckbox;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogInputs;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogSelect;
import com.fancyinnovations.fancydialogs.api.data.inputs.DialogTextField;
import com.fancyinnovations.fancydialogs.dialog.DialogImpl;
import com.fancyinnovations.fancydialogs.storage.DialogStorage;

import java.io.File;
import java.util.List;

public class DefaultDialogs {

    private static final DialogRegistry registry = FancyDialogsPlugin.get().getDialogRegistry();
    private static final DialogStorage storage = FancyDialogsPlugin.get().getDialogStorage();

    public static void registerDefaultDialogs() {
        File dialogsFolder = new File("plugins/FancyDialogs/data/dialogs");
        if (dialogsFolder.exists()) {
            return;
        }
        dialogsFolder.mkdirs();

        welcomeToFancyDialogsDialog();
        welcomeDialog();
        quickActions();
        conditionalDialogExample();
    }

    private static void welcomeToFancyDialogsDialog() {
        DialogData data = new DialogData(
                "welcome_to_fancydialogs",
                "<u><b><color:#ff7300>Welcome to FancyDialogs!</color></b></u>",
                false,
                List.of(
                        new DialogBodyData("<color:#ffd199><i>The simple and lightweight dialog plugin for your server!<i></color>"),
                        new DialogBodyData(""),
                        new DialogBodyData("This dialog is a demonstration of how to use FancyDialogs to create interactive and user-friendly dialogs."),
                        new DialogBodyData("FancyDialogs supports <rainbow>MiniMessages</rainbow> and PlaceholderAPI"),
                        new DialogBodyData("Explore more features in the documentation (<click:open_url:'https://docs.fancyinnovations.com/fancyholograms/'><u>click here</u></click>)."),
                        new DialogBodyData("<gradient:#ff7300:#ffd199:#ff7300>Enjoy using FancyDialogs :D</gradient>")
                ),
                new DialogInputs(
                        List.of(
                                new DialogTextField(
                                        "nickname",
                                        "<color:#ff7300>What is your nickname?</color>",
                                        1,
                                        "",
                                        50,
                                        1
                                )
                        ),
                        List.of(
                                new DialogSelect(
                                        "color_choice",
                                        "<color:#ff7300>Choose your favorite color</color>",
                                        2,
                                        List.of(
                                                new DialogSelect.Entry("red", "<color:red>Red</color>", true),
                                                new DialogSelect.Entry("green", "<color:green>Green</color>", false),
                                                new DialogSelect.Entry("blue", "<color:blue>Blue</color>", false),
                                                new DialogSelect.Entry("yellow", "<color:yellow>Yellow</color>", false)
                                        )
                                )
                        ),
                        List.of(
                                new DialogCheckbox(
                                        "is_cool",
                                        "<color:#ff7300>Are you cool?</color>",
                                        3,
                                        true
                                )
                        )
                ),
                List.of(
                        new DialogButton(
                                "<color:#ff4f19>Close</color>",
                                "<color:#ff4f19>Enjoy using FancyDialogs</color>",
                                List.of()
                        ),
                        new DialogButton(
                                "<color:#ffd000>Show favourite color</color>",
                                "<color:#ff4f19>Click to show your fav color :D</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Hi {nickname}, your favorite color is: <color:{color_choice}>{color_choice}</color>")
                                )
                        ),
                        new DialogButton(
                                "<color:#ffd000>Check if you are cool</color>",
                                "<color:#ff4f19>Click to know if, you are cool</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Hi {nickname}, are you cool: {is_cool}")
                                )
                        )
                )
        );

        register(data);
    }

    private static void welcomeDialog() {
        DialogData data = new DialogData(
                "welcome",
                "<b><color:#00ff5e>Welcome to {server_name}!</color></b>",
                false,
                List.of(
                        new DialogBodyData("<color:#a8ffb4><i>The best Minecraft server on earth!<i></color>"),
                        new DialogBodyData(""),
                        new DialogBodyData("We are glad to have you here!"),
                        new DialogBodyData("If you have any questions, feel free to ask our staff members.")
                ),
                DialogInputs.EMPTY,
                List.of(
                        new DialogButton(
                                "<color:red>Read the rules</color>",
                                "<color:red>Click to read our rules!</color>",
                                List.of(
                                        new DialogButton.DialogAction("open_dialog", "rules")
                                )
                        ),
                        new DialogButton(
                                "<color:#00ff5e>Start playing</color>",
                                "<color:#00ff5e>Click to start playing!</color>",
                                List.of()
                        ),
                        new DialogButton(
                                "<color:#1787ff>Join our Discord</color>",
                                "<color:#1787ff>Click to join our Discord server!</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Join our Discord server here: LINK TO DISCORD")
                                )
                        ),
                        new DialogButton(
                                "<color:#ffee00>Visit our website</color>",
                                "<color:#ffee00>Click to visit our website!</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Visit our website here: LINK TO WEBSITE")
                                )
                        )
                )
        );

        register(data);
    }

    private static void quickActions() {
        DialogData data = new DialogData(
                "quick_actions",
                "<u><b><color:gold>Quick Actions</color></b></u>",
                false,
                List.of(
                        new DialogBodyData("Here you can quickly access some of the most important features of our server.")
                ),
                DialogInputs.EMPTY,
                List.of(
                        new DialogButton(
                                "<color:#ffee00>Visit our website</color>",
                                "<color:#ffee00>Click to visit our website!</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Visit our website here: LINK TO WEBSITE")
                                )
                        ),
                        new DialogButton(
                                "<color:#ffee00>Read the rules</color>",
                                "<color:#ffee00>Click to read our rules!</color>",
                                List.of(
                                        new DialogButton.DialogAction("open_dialog", "rules")
                                )
                        ),
                        new DialogButton(
                                "<color:#ffee00>Join our Discord</color>",
                                "<color:#ffee00>Click to join our Discord server!</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Join our Discord server here: LINK TO DISCORD")
                                )
                        ),
                        new DialogButton(
                                "<color:#ffee00>Support us</color>",
                                "<color:#ffee00>Click to support us!</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Support us by donating here: LINK TO DONATE")
                                )
                        ),
                        new DialogButton(
                                "<color:red>Close</color>",
                                "<color:red>Click to close this dialog!</color>",
                                List.of()
                        )
                )
        );

        register(data);
    }

    private static void conditionalDialogExample() {
        DialogData data = new DialogData(
                "conditional_dialog_example",
                "<u><b><color:#00ddff>Conditional Dialog Example</color></b></u>",
                true,
                List.of(
                        new DialogBodyData("<color:#b3f0ff>This dialog demonstrates conditional features:</color>"),
                        new DialogBodyData("• Buttons with permission requirements"),
                        new DialogBodyData("• Inputs with PlaceholderAPI conditions"),
                        new DialogBodyData("• Dynamic checkbox states based on conditions")
                ),
                new DialogInputs(
                        List.of(
                                // Text field visible only to admins
                                new DialogTextField(
                                        "admin_note",
                                        "<color:#ff0000>Admin Note (Only visible to admins)</color>",
                                        1,
                                        "Enter admin note...",
                                        100,
                                        2,
                                        List.of(new DialogCondition(ConditionType.PERMISSION, "fancydialogs.admin"))
                                ),
                                // Text field visible to all players
                                new DialogTextField(
                                        "player_message",
                                        "<color:#00ff00>Your Message</color>",
                                        2,
                                        "Type something...",
                                        50,
                                        1
                                )
                        ),
                        List.of(
                                // Select visible only when player level >= 10
                                new DialogSelect(
                                        "rank_choice",
                                        "<color:#ffaa00>Choose Your Rank (Level 10+ required)</color>",
                                        3,
                                        List.of(
                                                new DialogSelect.Entry("warrior", "<color:#ff0000>Warrior</color>", true),
                                                new DialogSelect.Entry("mage", "<color:#0000ff>Mage</color>", false),
                                                new DialogSelect.Entry("archer", "<color:#00ff00>Archer</color>", false)
                                        ),
                                        List.of(new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%player_level%", "10"))
                                ),
                                // Select with dynamic default option based on PAPI value
                                // Example: If %player_gamemode% equals "1", "ON" is selected; if "0", "OFF" is selected
                                new DialogSelect(
                                        "toggle_status",
                                        "<color:#00ddff>Toggle Status (Changes based on your game mode)</color>",
                                        4,
                                        List.of(
                                                // "ON" option - selected when gamemode is creative (1)
                                                new DialogSelect.Entry(
                                                        "on",
                                                        "<color:#00ff00>✓ ON</color>",
                                                        false, // default to false
                                                        List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_gamemode%", "1"))
                                                ),
                                                // "OFF" option - selected when gamemode is survival (0)
                                                new DialogSelect.Entry(
                                                        "off",
                                                        "<color:#ff0000>✗ OFF</color>",
                                                        false, // default to false
                                                        List.of(new DialogCondition(ConditionType.PAPI_EQUALS, "%player_gamemode%", "0"))
                                                )
                                        )
                                )
                        ),
                        List.of(
                                // Checkbox that is automatically checked if player has VIP permission
                                new DialogCheckbox(
                                        "vip_status",
                                        "<color:#ffd700>VIP Status (Auto-checked if you have VIP)</color>",
                                        5,
                                        false,
                                        null,
                                        List.of(new DialogCondition(ConditionType.PERMISSION, "server.vip"))
                                ),
                                // Checkbox visible only if player is NOT banned
                                new DialogCheckbox(
                                        "agree_rules",
                                        "<color:#00ff00>I agree to follow server rules</color>",
                                        6,
                                        false,
                                        List.of(new DialogCondition(ConditionType.PAPI_NOT_EQUALS, "%player_is_banned%", "yes")),
                                        null
                                )
                        )
                ),
                List.of(
                        // Button visible only to admins
                        new DialogButton(
                                "<color:#ff0000>Admin Panel</color>",
                                "<color:#ff0000>Only admins can see this button</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Opening admin panel...")
                                ),
                                List.of(new DialogCondition(ConditionType.PERMISSION, "fancydialogs.admin"))
                        ),
                        // Button visible only to VIPs
                        new DialogButton(
                                "<color:#ffd700>VIP Features</color>",
                                "<color:#ffd700>Access VIP exclusive features</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Welcome VIP player!")
                                ),
                                List.of(new DialogCondition(ConditionType.PERMISSION, "server.vip"))
                        ),
                        // Button visible when player level >= 5
                        new DialogButton(
                                "<color:#00ff00>Level 5+ Reward</color>",
                                "<color:#00ff00>Claim your level 5 reward</color>",
                                List.of(
                                        new DialogButton.DialogAction("console_command", "give {player} diamond 5")
                                ),
                                List.of(new DialogCondition(ConditionType.PAPI_GREATER_OR_EQUAL, "%player_level%", "5"))
                        ),
                        // Button visible when player does NOT have a specific permission
                        new DialogButton(
                                "<color:#ffaa00>Get Started Guide</color>",
                                "<color:#ffaa00>For new players</color>",
                                List.of(
                                        new DialogButton.DialogAction("message", "Welcome new player! Here's a guide...")
                                ),
                                List.of(new DialogCondition(ConditionType.NO_PERMISSION, "server.veteran"))
                        ),
                        // Button always visible
                        new DialogButton(
                                "<color:#aaaaaa>Close</color>",
                                "<color:#aaaaaa>Close this dialog</color>",
                                List.of()
                        )
                )
        );

        register(data);
    }

    private static void register(DialogData dialogData) {
        DialogImpl dialog = new DialogImpl(dialogData.id(), dialogData);
        storage.save(dialogData);
        registry.register(dialog);
    }

}
