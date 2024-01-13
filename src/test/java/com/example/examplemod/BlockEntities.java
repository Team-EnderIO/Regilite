package com.example.examplemod;

import com.example.examplemod.exampleclasses.ExampleBlockentity;
import com.example.regilite.registry.EnderBlockEntityRegistry;
import com.example.regilite.registry.EnderDeferredBlockEntity;
import net.neoforged.bus.api.IEventBus;

public class BlockEntities {
    public static final EnderBlockEntityRegistry BLOCK_ENTITIES = EnderBlockEntityRegistry.create(ExampleMod.MODID);

    public static EnderDeferredBlockEntity<ExampleBlockentity> EXAMPLE_BLOCKENTITY = BLOCK_ENTITIES.registerBlockEntity("example", ExampleBlockentity::new, Blocks.EXAMPLE_BLOCK);

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
