package com.fancyinnovations.fancydialogs.api.data.inputs;

import com.fancyinnovations.fancydialogs.api.data.condition.DialogCondition;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DialogTextField extends DialogInput {

    private final String placeholder;
    private final int maxLength;
    private final int maxLines;

    public DialogTextField(String key, String label, int order, String placeholder, int maxLength, int maxLines) {
        this(key, label, order, placeholder, maxLength, maxLines, null);
    }

    public DialogTextField(String key, String label, int order, String placeholder, int maxLength, int maxLines, @Nullable List<DialogCondition> visibilityConditions) {
        super(key, label, order, visibilityConditions);
        this.placeholder = placeholder;
        this.maxLength = maxLength;
        this.maxLines = maxLines;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMaxLines() {
        return maxLines;
    }
}
