package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.registry.BlockEntityRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.util.NonNullFunction;
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
                Supplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer = regiliteBlockEntity.getRenderer();
                if (renderer != null) {
                    var blockEntityType = (BlockEntityType<T>)be.get();
                    event.registerBlockEntityRenderer(blockEntityType, (c) -> renderer.get().apply(c));
                }
            }
        }
    }

    public void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        this.registerGenericBER(event);
    }
}
