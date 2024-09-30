package com.enderio.regilite.entities;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

class RegiliteClientEntityTypes {
    private final RegiliteEntityTypes regiliteEntityTypes;

    public RegiliteClientEntityTypes(RegiliteEntityTypes regiliteEntityTypes) {
        this.regiliteEntityTypes = regiliteEntityTypes;
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var entity : regiliteEntityTypes.entities) {
            registerRenderer(event, entity);
        }
    }

    private <T extends Entity> void registerRenderer(EntityRenderersEvent.RegisterRenderers event, EntityTypeBuilder<T> builder) {
        if (builder.rendererFactory != null) {
            event.registerEntityRenderer(builder.get(), ctx -> builder.rendererFactory.get().apply(ctx));
        }
    }
}
