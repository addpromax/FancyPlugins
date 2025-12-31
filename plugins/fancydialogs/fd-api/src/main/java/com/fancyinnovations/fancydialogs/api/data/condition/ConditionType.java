package com.fancyinnovations.fancydialogs.api.data.condition;

/**
 * Types of conditions that can be evaluated for dialogs
 */
public enum ConditionType {
    /**
     * Check if player has a permission node
     */
    PERMISSION,
    
    /**
     * Check if player does NOT have a permission node
     */
    NO_PERMISSION,
    
    /**
     * Check if PlaceholderAPI value equals a specific value
     */
    PAPI_EQUALS,
    
    /**
     * Check if PlaceholderAPI value does not equal a specific value
     */
    PAPI_NOT_EQUALS,
    
    /**
     * Check if PlaceholderAPI value is greater than a specific value (numeric comparison)
     */
    PAPI_GREATER_THAN,
    
    /**
     * Check if PlaceholderAPI value is less than a specific value (numeric comparison)
     */
    PAPI_LESS_THAN,
    
    /**
     * Check if PlaceholderAPI value is greater than or equal to a specific value (numeric comparison)
     */
    PAPI_GREATER_OR_EQUAL,
    
    /**
     * Check if PlaceholderAPI value is less than or equal to a specific value (numeric comparison)
     */
    PAPI_LESS_OR_EQUAL,
    
    /**
     * Check if PlaceholderAPI value contains a specific string
     */
    PAPI_CONTAINS,
    
    /**
     * Check if PlaceholderAPI value starts with a specific string
     */
    PAPI_STARTS_WITH,
    
    /**
     * Check if PlaceholderAPI value ends with a specific string
     */
    PAPI_ENDS_WITH
}
