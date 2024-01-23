package com.example.regilite.events;

import com.example.regilite.registry.BlockEntityRegistry;
import com.example.regilite.holder.RegiliteBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class BlockEntityRendererEvents {

    private final BlockEntityRegistry registry;

    public BlockEntityRendererEvents(BlockEntityRegistry registry) {
        this.registry = registry;
    }

    // TODO: These casts should be checked thoroughly.
    @SuppressWarnings("unchecked")
    private <T extends BlockEntity> void registerGenericBER(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : registry.getEntries()) {
            //noinspection rawtypes
            if (be instanceof RegiliteBlockEntity regiliteBlockEntity) {
                Supplier<BlockEntityRendererProvider<T>> renderer = regiliteBlockEntity.getRenderer();
                if (renderer != null) {
                    var blockEntityType = (BlockEntityType<T>)be.get();
                    event.registerBlockEntityRenderer(blockEntityType, renderer.get());
                }
            }
        }
    }

    public void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        this.registerGenericBER(event);
    }
}
