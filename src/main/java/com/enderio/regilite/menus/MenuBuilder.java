package com.enderio.regilite.menus;

import com.enderio.regilite.RegiliteBuilder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class MenuBuilder<T extends AbstractContainerMenu> extends RegiliteBuilder<MenuBuilder<T>, MenuType<?>, MenuType<T>, DeferredHolder<MenuType<?>, MenuType<T>>> {

    protected Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor;

    protected MenuBuilder(DeferredHolder<MenuType<?>, MenuType<T>> holder) {
        super(holder);
    }

    public MenuBuilder<T> screen(Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }
}
