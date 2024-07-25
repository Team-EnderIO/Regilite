/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.blocks.RegiliteBlockLootProvider;
import com.enderio.regilite.examplemod.exampleclasses.ExampleBlock;
import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;

public class Blocks {
    public static final DeferredBlock<ExampleBlock> EXAMPLE_BLOCK = ExampleMod.REGILITE.blocks()
            .create("example_block", ExampleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .tags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .translation("Test Example Block")
            .blockColor(() -> () -> ExampleColors.BLOCK)
            .blockStateProvider((prov, ctx) -> prov.simpleBlock(ctx.get()))
            .lootTable(RegiliteBlockLootProvider::dropSelf)
            .createSimpleBlockItem(item -> item
                .tags(ItemTags.PLANKS)
                .modelProvider((prov, ctx) -> prov.basicItem(ctx.get()))
                .tab(CreativeModeTabs.BUILDING_BLOCKS)
                .tab(CreativeTabs.EXAMPLE_TAB.getKey())
            )
            .finishHolder();

    public static void register() {
    }
}
