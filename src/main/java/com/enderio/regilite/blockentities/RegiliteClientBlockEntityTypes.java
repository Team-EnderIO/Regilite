package com.enderio.regilite.blockentities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

class RegiliteClientBlockEntityTypes {
    private final RegiliteBlockEntityTypes regiliteBlockEntityTypes;

    public RegiliteClientBlockEntityTypes(RegiliteBlockEntityTypes regiliteBlockEntityTypes) {
        this.regiliteBlockEntityTypes = regiliteBlockEntityTypes;
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var blockEntity : regiliteBlockEntityTypes.blockEntities) {
            registerRenderer(event, blockEntity);
        }
    }

    private <T extends BlockEntity> void registerRenderer(EntityRenderersEvent.RegisterRenderers event, BlockEntityTypeBuilder<T> builder) {
        if (builder.rendererFactory != null) {
            event.registerBlockEntityRenderer(builder.get(), ctx -> builder.rendererFactory.get().apply(ctx));
        }
    }
}
