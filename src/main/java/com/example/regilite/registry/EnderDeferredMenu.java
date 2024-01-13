package com.example.regilite.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class EnderDeferredMenu<T extends AbstractContainerMenu> extends DeferredHolder<MenuType<? extends AbstractContainerMenu>, MenuType<T>> {

    private Supplier<MenuScreens.ScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor;

    protected EnderDeferredMenu(ResourceKey<MenuType<? extends AbstractContainerMenu>> key) {
        super(key);
    }

    public EnderDeferredMenu<T> setScreenConstructor(Supplier<MenuScreens.ScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }

    public Supplier<MenuScreens.ScreenConstructor<T, ? extends AbstractContainerScreen<T>>> getScreenConstructor() {
        return screenConstructor;
    }

    public static <I extends AbstractContainerMenu> EnderDeferredMenu<I> createMenu(ResourceKey<MenuType<? extends AbstractContainerMenu>> menuTypeResourceKey) {
        return new EnderDeferredMenu<>(menuTypeResourceKey);
    }
}