/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.data.RegiliteBlockLootProvider;
import com.enderio.regilite.examplemod.exampleclasses.ExampleBlock;
import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;

public class Blocks {
    public static final BlockRegistry BLOCKS = ExampleMod.getRegilite().blockRegistry();
    public static final ItemRegistry ITEMS = ExampleMod.getRegilite().itemRegistry();

    public static final RegiliteBlock<ExampleBlock> EXAMPLE_BLOCK = BLOCKS
            .registerBlock("example_block", ExampleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .withTags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .withTranslation("Test Example Block")
            .withBlockColor(() -> () -> ExampleColors.BLOCK)
            .withBlockStateProvider((prov, ctx) -> prov.simpleBlock(ctx.get()))
            .withLootTable(RegiliteBlockLootProvider::dropSelf)
            .withBlockItem(ITEMS, item -> item
                    .withTags(ItemTags.PLANKS)
                    .withModelProvider((prov, ctx) -> prov.basicItem(ctx.get()))
                    .withTab(CreativeModeTabs.BUILDING_BLOCKS)
                    .withTab(CreativeTabs.EXAMPLE_TAB.getKey()));

    public static final DeferredBlock<ExampleBlock> EXAMPLE_BLOCK_DEMO = ExampleMod.REGILITE.blocks()
            .create("example_block_demo", ExampleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .withTags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .withTranslation("Test Example Block")
            .withBlockColor(() -> () -> ExampleColors.BLOCK)
            //.withBlockStateProvider((prov, ctx) -> prov.simpleBlock(ctx.get()))
            .withLootTable(com.enderio.regilite.blocks.RegiliteBlockLootProvider::dropSelf)
            /*.withSimpleBlockItem(item -> item
                .withTags(ItemTags.PLANKS)
                //.withModelProvider((prov, ctx) -> prov.basicItem(ctx.get()))
                .withTab(CreativeModeTabs.BUILDING_BLOCKS)
                .withTab(CreativeTabs.EXAMPLE_TAB.getKey()))*/
            .finishHolder();

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
