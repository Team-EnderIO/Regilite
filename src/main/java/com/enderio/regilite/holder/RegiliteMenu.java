package com.enderio.regilite.holder;

import com.enderio.regilite.events.IScreenConstructor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class RegiliteMenu<T extends AbstractContainerMenu> extends DeferredHolder<MenuType<? extends AbstractContainerMenu>, MenuType<T>> {

    private Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor;

    protected RegiliteMenu(ResourceKey<MenuType<? extends AbstractContainerMenu>> key) {
        super(key);
    }

    public RegiliteMenu<T> setScreenConstructor(Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }

    public IScreenConstructor<T, ? extends AbstractContainerScreen<T>> getScreenConstructor() {
        return screenConstructor.get();
    }

    public static <I extends AbstractContainerMenu> RegiliteMenu<I> createMenu(ResourceKey<MenuType<? extends AbstractContainerMenu>> menuTypeResourceKey) {
        return new RegiliteMenu<>(menuTypeResourceKey);
    }
}
