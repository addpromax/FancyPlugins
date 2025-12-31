package com.fancyinnovations.fancydialogs.api.data.inputs;

import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class DialogInput {

    protected final String key;
    protected final String label;
    protected final int order;
    protected final List<DialogCondition> visibilityConditions;

    public DialogInput(String key, String label, int order) {
        this(key, label, order, null);
    }

    public DialogInput(String key, String label, int order, @Nullable List<DialogCondition> visibilityConditions) {
        this.key = key;
        this.label = label;
        this.order = order;
        this.visibilityConditions = visibilityConditions;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public int getOrder() {
        return order;
    }

    @Nullable
    public List<DialogCondition> getVisibilityConditions() {
        return visibilityConditions;
    }
}
