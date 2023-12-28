package com.example.examplemod.registry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;
import oshi.util.tuples.Pair;

import java.util.function.Supplier;

public class EnderDeferredMenu<T extends AbstractContainerMenu> extends DeferredHolder<MenuType<? extends AbstractContainerMenu>, MenuType<T>> implements ITranslatable{

    protected String translation = StringUtils.capitalize(getId().getPath().replace('_', ' '));
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

    public EnderDeferredMenu<T> setTranslation(String translation) {
        this.translation = translation;
        return this;
    }

    @Override
    public Pair<String, String> getTranslation() {
        return new Pair<>(this.get().toString(), translation);
    }

    public static <T extends AbstractContainerMenu> EnderDeferredMenu<T> createMenu(DeferredHolder<MenuType<?>, MenuType<T>> holder) {
        return new EnderDeferredMenu<>(holder.getKey());
    }
}
