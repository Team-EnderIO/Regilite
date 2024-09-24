package com.enderio.regilite.entities;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

class RegiliteClientEntities {
    private final RegiliteEntities regiliteEntities;

    public RegiliteClientEntities(RegiliteEntities regiliteEntities) {
        this.regiliteEntities = regiliteEntities;
    }

    @SubscribeEvent
    public void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var entity : regiliteEntities.entities) {
            registerRenderer(event, entity);
        }
    }

    private <T extends Entity> void registerRenderer(EntityRenderersEvent.RegisterRenderers event, EntityBuilder<T> builder) {
        if (builder.rendererFactory != null) {
            event.registerEntityRenderer(builder.get(), ctx -> builder.rendererFactory.get().apply(ctx));
        }
    }
}
