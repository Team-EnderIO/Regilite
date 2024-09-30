/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.menus;

import com.enderio.regilite.RegiliteBuilder;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class MenuTypeBuilder<T extends AbstractContainerMenu> extends RegiliteBuilder<MenuTypeBuilder<T>, MenuType<?>, MenuType<T>, DeferredHolder<MenuType<?>, MenuType<T>>> {

    protected Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor;

    protected MenuTypeBuilder(DeferredHolder<MenuType<?>, MenuType<T>> holder) {
        super(holder);
    }

    public MenuTypeBuilder<T> screen(Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }
}
