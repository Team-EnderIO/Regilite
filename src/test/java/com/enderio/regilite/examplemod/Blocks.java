/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleBlock;
import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import com.enderio.regilite.data.RegiliteBlockLootProvider;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;

public class Blocks {
    public static final BlockRegistry BLOCKS =  ExampleMod.getRegilite().blockRegistry();
    public static final ItemRegistry ITEMS =  ExampleMod.getRegilite().itemRegistry();

    public static final RegiliteBlock<ExampleBlock> EXAMPLE_BLOCK = BLOCKS
            .registerBlock("example_block", ExampleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .withTags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .withTranslation("Test Example Block")
            .withBlockColor(() -> () -> ExampleColors.BLOCK)
            .setBlockStateProvider((prov, ctx) -> prov.simpleBlock(ctx.get()))
            .withLootTable(RegiliteBlockLootProvider::dropSelf)
            .withBlockItem(ITEMS, item -> item
                    .withTags(ItemTags.PLANKS)
                    .setModelProvider((prov, ctx) -> prov.basicItem(ctx.get()))
                    .withTab(CreativeModeTabs.BUILDING_BLOCKS)
                    .withTab(CreativeTabs.EXAMPLE_TAB.getKey()));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
