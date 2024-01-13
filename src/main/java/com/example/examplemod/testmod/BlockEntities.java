package com.example.examplemod.testmod;

import com.example.examplemod.registry.EnderBlockEntityRegistry;
import com.example.examplemod.registry.EnderDeferredBlockEntity;
import com.example.examplemod.testmod.exampleclasses.ExampleBlockentity;
import net.neoforged.bus.api.IEventBus;

public class BlockEntities {
    public static final EnderBlockEntityRegistry BLOCK_ENTITIES = EnderBlockEntityRegistry.create(ExampleMod.MODID);

    public static EnderDeferredBlockEntity<ExampleBlockentity> EXAMPLE_BLOCKENTITY = BLOCK_ENTITIES.registerBlockEntity("example", ExampleBlockentity::new, Blocks.EXAMPLE_BLOCK);

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
