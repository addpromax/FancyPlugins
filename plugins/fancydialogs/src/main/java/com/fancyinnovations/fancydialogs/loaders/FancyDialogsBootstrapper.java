package com.fancyinnovations.fancydialogs.loaders;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.DialogTagKeys;
import io.papermc.paper.registry.tag.TagEntry;
import io.papermc.paper.tag.PreFlattenTagRegistrar;
import net.kyori.adventure.key.Key;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FancyDialogsBootstrapper implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext bootstrapContext) {
        // Register the lifecycle event for pre-flatten tag registration for Dialogs
        final LifecycleEventManager<BootstrapContext> manager = bootstrapContext.getLifecycleManager();
        
        // Register for DIALOG tags pre-flatten event
        manager.registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.DIALOG), event -> {
            final PreFlattenTagRegistrar<Dialog> registrar = event.registrar();
            
            // Add the quick_actions dialog to the #quick_actions tag
            // This allows the client to automatically show this dialog when the player presses 'G'
            registrar.addToTag(
                    DialogTagKeys.QUICK_ACTIONS,
                    Set.of(TagEntry.valueEntry(Key.key("fancydialogs:quick_actions")))
            );
        });
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return PluginBootstrap.super.createPlugin(context);
    }
}
