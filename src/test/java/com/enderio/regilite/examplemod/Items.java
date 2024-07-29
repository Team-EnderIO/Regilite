/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

public class Items {
    public static final DeferredItem<Item> EXAMPLE_ITEM = ExampleMod.REGILITE.items()
        .createSimple("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()))
        .tags(ItemTags.WOOL)
        .translation("Test Example Item")
        .itemColor(() -> () -> ExampleColors.ITEM)
        .asHolder();

    public static void register() {
    }
}
