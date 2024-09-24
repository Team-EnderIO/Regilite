package com.enderio.regilite.blockentities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

class RegiliteClientBlockEntities {
    private final RegiliteBlockEntities regiliteBlockEntities;

    public RegiliteClientBlockEntities(RegiliteBlockEntities regiliteBlockEntities) {
        this.regiliteBlockEntities = regiliteBlockEntities;
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var blockEntity : regiliteBlockEntities.blockEntities) {
            registerRenderer(event, blockEntity);
        }
    }

    private <T extends BlockEntity> void registerRenderer(EntityRenderersEvent.RegisterRenderers event, BlockEntityBuilder<T> builder) {
        if (builder.rendererFactory != null) {
            event.registerBlockEntityRenderer(builder.get(), ctx -> builder.rendererFactory.get().apply(ctx));
        }
    }
}
