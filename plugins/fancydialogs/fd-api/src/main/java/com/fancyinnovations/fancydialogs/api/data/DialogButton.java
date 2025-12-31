package com.fancyinnovations.fancydialogs.api.data;

import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DialogButton {

    private final String label;
    private final String tooltip;
    private final List<DialogAction> actions;
    private final List<DialogCondition> visibilityConditions;
    private transient String id;

    public DialogButton(String label, String tooltip, List<DialogAction> actions) {
        this(label, tooltip, actions, null);
    }

    public DialogButton(String label, String tooltip, List<DialogAction> actions, @Nullable List<DialogCondition> visibilityConditions) {
        this.id = UUID.randomUUID().toString();
        this.label = label;
        this.tooltip = tooltip;
        this.actions = actions;
        this.visibilityConditions = visibilityConditions;
    }

    public String id() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    public String label() {
        return label;
    }

    public String tooltip() {
        return tooltip;
    }

    public List<DialogAction> actions() {
        return actions;
    }

    @Nullable
    public List<DialogCondition> visibilityConditions() {
        return visibilityConditions;
    }

    public record DialogAction(
            String name,
            String data
    ) {

    }

}
