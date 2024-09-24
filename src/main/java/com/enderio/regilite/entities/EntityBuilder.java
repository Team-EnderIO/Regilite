package com.enderio.regilite.entities;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.tags.RegiliteTags;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityBuilder<T extends Entity> extends RegiliteBuilder<EntityBuilder<T>, EntityType<?>, EntityType<T>, DeferredHolder<EntityType<?>, EntityType<T>>> {

    private final RegiliteTags tagsModule;

    protected Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> rendererFactory;

    private final List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    protected EntityBuilder(DeferredHolder<EntityType<?>, EntityType<T>> holder, RegiliteTags tagsModule) {
        super(holder);
        this.tagsModule = tagsModule;
    }

    public EntityBuilder<T> tag(TagKey<EntityType<?>> tag) {
        tagsModule.entityTypes().tag(tag).add(this::get);
        return this;
    }

    @SafeVarargs
    public final EntityBuilder<T> tags(TagKey<EntityType<?>>... tags) {
        tagsModule.entityTypes().addToTags(this::get, tags);
        return this;
    }

    public EntityBuilder<T> renderer(Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> rendererFactory) {
        this.rendererFactory = rendererFactory;
        return this;
    }

    public <TCap, TContext> EntityBuilder<T> capability(EntityCapability<TCap, TContext> capability, ICapabilityProvider<? super T, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    void attachCapabilities(RegisterCapabilitiesEvent event) {
        for (var attachedCapability : attachedCapabilityList) {
            attachedCapability.registerProvider(event, get());
        }
    }

    protected record AttachedCapability<T extends Entity, TCap, TContext>(
            EntityCapability<TCap, TContext> capability,
            ICapabilityProvider<? super T, TContext, TCap> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, EntityType<T> type) {
            event.registerEntity(capability, type, provider);
        }
    }
}
