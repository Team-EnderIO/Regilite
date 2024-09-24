/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.blocks;

import com.enderio.regilite.data.DataGenContext;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class RegiliteBlockStateProvider extends BlockStateProvider {
    private final RegiliteBlocks blocks;

    public RegiliteBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, RegiliteBlocks blocks) {
        super(output, modid, exFileHelper);
        this.blocks = blocks;
    }

    @Override
    protected void registerStatesAndModels() {
        blocks.blockBuilders().forEach(this::registerState);
    }

    private <T extends Block> void registerState(BlockBuilder<T> blockBuilder) {
        var blockStateProvider = blockBuilder.blockStateProvider;
        if (blockStateProvider != null) {
            blockStateProvider.accept(this, new DataGenContext<>(blockBuilder.getId(), blockBuilder::get));
        }
    }
}
