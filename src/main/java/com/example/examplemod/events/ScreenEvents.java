package com.example.examplemod.events;

import com.example.examplemod.registry.EnderDeferredMenu;
import com.example.examplemod.registry.EnderMenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ScreenEvents {

    private final EnderMenuRegistry registry;

    public ScreenEvents(EnderMenuRegistry registry) {
        this.registry = registry;
    }

    public <T extends AbstractContainerMenu> void genericScreenEvent(FMLClientSetupEvent event) {
        for (DeferredHolder<MenuType<?>, ? extends MenuType<?>> menu : registry.getEntries()) {
            Supplier<? extends MenuScreens.ScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screen = ((EnderDeferredMenu<T>) menu).getScreenConstructor();
            if (screen != null) {
                event.enqueueWork(
                        () -> MenuScreens.register(((EnderDeferredMenu<T>) menu).get(), screen.get())
                );
            }
        }
    }

    public void screenEvent(FMLClientSetupEvent event) {
        this.genericScreenEvent(event);
    }
}
