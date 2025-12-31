package com.fancyinnovations.fancydialogs.api.data.condition;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a condition that must be met for a dialog element to be visible or active
 */
public class DialogCondition {
    
    private final ConditionType type;
    private final String value;
    private final String compareValue;
    
    /**
     * Creates a condition for permission checks
     * 
     * @param type PERMISSION or NO_PERMISSION
     * @param value The permission node to check (e.g., "server.admin")
     */
    public DialogCondition(@NotNull ConditionType type, @NotNull String value) {
        this(type, value, null);
    }
    
    /**
     * Creates a condition for PlaceholderAPI comparisons
     * 
     * @param type The comparison type (PAPI_EQUALS, PAPI_GREATER_THAN, etc.)
     * @param value The PlaceholderAPI placeholder (e.g., "%player_level%")
     * @param compareValue The value to compare against (e.g., "10")
     */
    public DialogCondition(@NotNull ConditionType type, @NotNull String value, @Nullable String compareValue) {
        this.type = type;
        this.value = value;
        this.compareValue = compareValue;
    }
    
    @NotNull
    public ConditionType getType() {
        return type;
    }
    
    @NotNull
    public String getValue() {
        return value;
    }
    
    @Nullable
    public String getCompareValue() {
        return compareValue;
    }
    
    /**
     * Check if this is a permission-based condition
     */
    public boolean isPermissionCondition() {
        return type == ConditionType.PERMISSION || type == ConditionType.NO_PERMISSION;
    }
    
    /**
     * Check if this is a PlaceholderAPI-based condition
     */
    public boolean isPapiCondition() {
        return !isPermissionCondition();
    }
}
