/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleBlockentity;
import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.registry.BlockEntityRegistry;
import net.neoforged.bus.api.IEventBus;

public class BlockEntities {
    public static final BlockEntityRegistry BLOCK_ENTITIES = ExampleMod.getRegilite().blockEntityRegistry();

    public static RegiliteBlockEntity<ExampleBlockentity> EXAMPLE_BLOCKENTITY = BLOCK_ENTITIES.registerBlockEntity("example", ExampleBlockentity::new, Blocks.EXAMPLE_BLOCK);

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
