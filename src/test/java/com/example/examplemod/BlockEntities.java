package com.example.examplemod;

import com.example.examplemod.exampleclasses.ExampleBlockentity;
import com.example.regilite.registry.BlockEntityRegistry;
import com.example.regilite.holder.RegiliteBlockEntity;
import net.neoforged.bus.api.IEventBus;

public class BlockEntities {
    public static final BlockEntityRegistry BLOCK_ENTITIES = BlockEntityRegistry.create(ExampleMod.MODID);

    public static RegiliteBlockEntity<ExampleBlockentity> EXAMPLE_BLOCKENTITY = BLOCK_ENTITIES.registerBlockEntity("example", ExampleBlockentity::new, Blocks.EXAMPLE_BLOCK);

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
