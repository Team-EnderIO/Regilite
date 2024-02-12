package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteEntity;
import com.enderio.regilite.registry.EntityRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class EntityRendererEvents {

    private final EntityRegistry registry;

    public EntityRendererEvents(EntityRegistry registry) {
        this.registry = registry;
    }

    // TODO: These casts should be checked thoroughly.
    @SuppressWarnings("unchecked")
    private <T extends Entity> void registerGenericER(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<EntityType<?>, ? extends EntityType<?>> e : registry.getEntries()) {
            //noinspection rawtypes
            if (e instanceof RegiliteEntity regiliteEntity) {
                Supplier<Supplier<EntityRendererProvider<T>>> renderer = regiliteEntity.getRenderer();
                if (renderer != null) {
                    var entityType = (EntityType<T>)e.get();
                    event.registerEntityRenderer(entityType, renderer.get().get());
                }
            }
        }
    }

    public void registerER(EntityRenderersEvent.RegisterRenderers event) {
        this.registerGenericER(event);
    }
}
