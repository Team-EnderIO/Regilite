/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public class Items {
    public static final ItemRegistry ITEMS = ExampleMod.getRegilite().itemRegistry();

    public static final RegiliteItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEdible().nutrition(1).saturationModifier(2f).build()))
            .withTags(ItemTags.WOOL)
            .withTranslation("Test Example Item")
            .withItemColor(() -> () -> ExampleColors.ITEM)
            .withTab(null);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
