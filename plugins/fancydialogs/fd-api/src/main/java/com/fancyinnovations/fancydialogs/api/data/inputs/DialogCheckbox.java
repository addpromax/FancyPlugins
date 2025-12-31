package com.fancyinnovations.fancydialogs.api.data.inputs;

import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DialogCheckbox extends DialogInput {

    private final boolean initial;
    private final List<DialogCondition> checkedConditions;

    public DialogCheckbox(String key, String label, int order, boolean initial) {
        this(key, label, order, initial, null, null);
    }

    public DialogCheckbox(String key, String label, int order, boolean initial, @Nullable List<DialogCondition> visibilityConditions, @Nullable List<DialogCondition> checkedConditions) {
        super(key, label, order, visibilityConditions);
        this.initial = initial;
        this.checkedConditions = checkedConditions;
    }

    public boolean isInitial() {
        return initial;
    }

    @Nullable
    public List<DialogCondition> getCheckedConditions() {
        return checkedConditions;
    }
}
