package com.example.regilite.events;

import com.example.regilite.registry.EnderBlockEntityRegistry;
import com.example.regilite.registry.EnderDeferredBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class BlockEntityRendererEvents {

    private final EnderBlockEntityRegistry registry;

    public BlockEntityRendererEvents(EnderBlockEntityRegistry registry) {
        this.registry = registry;
    }

    private <T extends BlockEntity> void registerGenericBER(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : registry.getEntries()) {
            if (be instanceof EnderDeferredBlockEntity) {
                Supplier<BlockEntityRendererProvider<T>> renderer = ((EnderDeferredBlockEntity<T>) be).getRenderer();
                if (renderer != null)
                    event.registerBlockEntityRenderer(((EnderDeferredBlockEntity<T>) be).get(), renderer.get());

            }
        }
    }

    public void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        this.registerGenericBER(event);
    }
}
