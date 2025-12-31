package com.fancyinnovations.fancydialogs.api.data.condition;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.chatcolorhandler.ChatColorHandler;
import org.lushplugins.chatcolorhandler.parsers.ParserTypes;

import java.util.List;

/**
 * Evaluates conditions for dialog elements
 */
public class ConditionEvaluator {
    
    /**
     * Evaluates a single condition for a player
     * 
     * @param condition The condition to evaluate
     * @param player The player to evaluate against
     * @return true if the condition is met, false otherwise
     */
    public static boolean evaluate(@NotNull DialogCondition condition, @NotNull Player player) {
        return switch (condition.getType()) {
            case PERMISSION -> player.hasPermission(condition.getValue());
            case NO_PERMISSION -> !player.hasPermission(condition.getValue());
            case PAPI_EQUALS -> evaluatePapiEquals(condition, player);
            case PAPI_NOT_EQUALS -> !evaluatePapiEquals(condition, player);
            case PAPI_GREATER_THAN -> evaluatePapiNumeric(condition, player, ComparisonType.GREATER);
            case PAPI_LESS_THAN -> evaluatePapiNumeric(condition, player, ComparisonType.LESS);
            case PAPI_GREATER_OR_EQUAL -> evaluatePapiNumeric(condition, player, ComparisonType.GREATER_OR_EQUAL);
            case PAPI_LESS_OR_EQUAL -> evaluatePapiNumeric(condition, player, ComparisonType.LESS_OR_EQUAL);
            case PAPI_CONTAINS -> evaluatePapiContains(condition, player);
            case PAPI_STARTS_WITH -> evaluatePapiStartsWith(condition, player);
            case PAPI_ENDS_WITH -> evaluatePapiEndsWith(condition, player);
        };
    }
    
    /**
     * Evaluates all conditions with AND logic (all must be true)
     * 
     * @param conditions The list of conditions to evaluate
     * @param player The player to evaluate against
     * @return true if all conditions are met, false otherwise
     */
    public static boolean evaluateAll(@Nullable List<DialogCondition> conditions, @NotNull Player player) {
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }
        
        for (DialogCondition condition : conditions) {
            if (!evaluate(condition, player)) {
                return false;
            }
        }
        
        return true;
    }
    
    private static boolean evaluatePapiEquals(@NotNull DialogCondition condition, @NotNull Player player) {
        String papiValue = parsePlaceholder(condition.getValue(), player);
        String compareValue = condition.getCompareValue();
        
        if (compareValue == null) {
            return false;
        }
        
        return papiValue.equalsIgnoreCase(compareValue);
    }
    
    private static boolean evaluatePapiContains(@NotNull DialogCondition condition, @NotNull Player player) {
        String papiValue = parsePlaceholder(condition.getValue(), player);
        String compareValue = condition.getCompareValue();
        
        if (compareValue == null) {
            return false;
        }
        
        return papiValue.toLowerCase().contains(compareValue.toLowerCase());
    }
    
    private static boolean evaluatePapiStartsWith(@NotNull DialogCondition condition, @NotNull Player player) {
        String papiValue = parsePlaceholder(condition.getValue(), player);
        String compareValue = condition.getCompareValue();
        
        if (compareValue == null) {
            return false;
        }
        
        return papiValue.toLowerCase().startsWith(compareValue.toLowerCase());
    }
    
    private static boolean evaluatePapiEndsWith(@NotNull DialogCondition condition, @NotNull Player player) {
        String papiValue = parsePlaceholder(condition.getValue(), player);
        String compareValue = condition.getCompareValue();
        
        if (compareValue == null) {
            return false;
        }
        
        return papiValue.toLowerCase().endsWith(compareValue.toLowerCase());
    }
    
    private static boolean evaluatePapiNumeric(@NotNull DialogCondition condition, @NotNull Player player, ComparisonType comparison) {
        String papiValue = parsePlaceholder(condition.getValue(), player);
        String compareValue = condition.getCompareValue();
        
        if (compareValue == null) {
            return false;
        }
        
        try {
            double papiNumber = Double.parseDouble(papiValue);
            double compareNumber = Double.parseDouble(compareValue);
            
            return switch (comparison) {
                case GREATER -> papiNumber > compareNumber;
                case LESS -> papiNumber < compareNumber;
                case GREATER_OR_EQUAL -> papiNumber >= compareNumber;
                case LESS_OR_EQUAL -> papiNumber <= compareNumber;
            };
        } catch (NumberFormatException e) {
            // If values are not numeric, return false
            return false;
        }
    }
    
    private static String parsePlaceholder(@NotNull String placeholder, @NotNull Player player) {
        // Use ChatColorHandler to parse PlaceholderAPI placeholders
        return ChatColorHandler.translate(placeholder, player, ParserTypes.placeholder());
    }
    
    private enum ComparisonType {
        GREATER,
        LESS,
        GREATER_OR_EQUAL,
        LESS_OR_EQUAL
    }
}
