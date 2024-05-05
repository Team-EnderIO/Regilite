package com.enderio.regilite.events;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;
import java.util.function.Supplier;

public class EntityRendererEvents {

    private final Regilite regilite;

    public EntityRendererEvents(Regilite regilite) {
        this.regilite = regilite;
    }

    // TODO: These casts should be checked thoroughly.
    @SuppressWarnings("unchecked")
    private <T extends Entity> void registerGenericER(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<EntityType<?>, ? extends EntityType<?>> e : regilite.getEntities()) {
            //noinspection rawtypes
            if (e instanceof RegiliteEntity regiliteEntity) {
                Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer = regiliteEntity.getRenderer();
                if (renderer != null) {
                    var entityType = (EntityType<T>)e.get();
                    event.registerEntityRenderer(entityType, (c) -> renderer.get().apply(c));
                }
            }
        }
    }

    public void registerER(EntityRenderersEvent.RegisterRenderers event) {
        this.registerGenericER(event);
    }
}
