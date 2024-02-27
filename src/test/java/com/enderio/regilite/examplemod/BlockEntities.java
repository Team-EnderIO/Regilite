package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleBlockentity;
import com.enderio.regilite.registry.BlockEntityRegistry;
import com.enderio.regilite.holder.RegiliteBlockEntity;
import net.neoforged.bus.api.IEventBus;

public class BlockEntities {
    public static final BlockEntityRegistry BLOCK_ENTITIES = BlockEntityRegistry.create(ExampleMod.getRegilite());

    public static RegiliteBlockEntity<ExampleBlockentity> EXAMPLE_BLOCKENTITY = BLOCK_ENTITIES.registerBlockEntity("example", ExampleBlockentity::new, Blocks.EXAMPLE_BLOCK);

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
