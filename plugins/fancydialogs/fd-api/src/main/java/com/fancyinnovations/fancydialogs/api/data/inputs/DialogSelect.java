package com.fancyinnovations.fancydialogs.api.data.inputs;

import com.fancyinnovations.fancydialogs.api.data.DialogButton;
import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DialogSelect extends DialogInput {

    private final List<Entry> options;

    public DialogSelect(String key, String label, int order, List<Entry> options) {
        this(key, label, order, options, null);
    }

    public DialogSelect(String key, String label, int order, List<Entry> options, @Nullable List<DialogCondition> visibilityConditions) {
        super(key, label, order, visibilityConditions);
        this.options = options;
    }

    public List<Entry> getOptions() {
        return options;
    }

    public static class Entry {
        private final String value;
        private final String display;
        private final boolean initial;
        private final List<DialogCondition> selectedConditions;
        private final List<DialogButton.DialogAction> actions;

        public Entry(String value, String display, boolean initial) {
            this(value, display, initial, null, null);
        }

        public Entry(String value, String display, boolean initial, @Nullable List<DialogCondition> selectedConditions) {
            this(value, display, initial, selectedConditions, null);
        }

        public Entry(String value, String display, boolean initial, @Nullable List<DialogCondition> selectedConditions, @Nullable List<DialogButton.DialogAction> actions) {
            this.value = value;
            this.display = display;
            this.initial = initial;
            this.selectedConditions = selectedConditions;
            this.actions = actions;
        }

        public String value() {
            return value;
        }

        public String display() {
            return display;
        }

        public boolean initial() {
            return initial;
        }

        @Nullable
        public List<DialogCondition> selectedConditions() {
            return selectedConditions;
        }

        @Nullable
        public List<DialogButton.DialogAction> actions() {
            return actions;
        }
    }

}
