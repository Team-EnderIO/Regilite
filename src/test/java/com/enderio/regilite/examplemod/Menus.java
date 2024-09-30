/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleMenu;
import com.enderio.regilite.examplemod.exampleclasses.ExampleScreen;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class Menus {

    public static final DeferredHolder<MenuType<?>, MenuType<ExampleMenu>> EXAMPLE_MENU = ExampleMod.REGILITE.menuTypes()
            .create("example", ExampleMenu::new, () -> ExampleScreen::new).finish();

    public static void register() {
    }
}
