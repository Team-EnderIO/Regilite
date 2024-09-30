/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleBlockentity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BlockEntities {
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ExampleBlockentity>> EXAMPLE_BLOCKENTITY = ExampleMod.REGILITE.blockEntityTypes()
            .create("example", ExampleBlockentity::new, Blocks.EXAMPLE_BLOCK)
            .finish();

    public static void register() {
    }
}
