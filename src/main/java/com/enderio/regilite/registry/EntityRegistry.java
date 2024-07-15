/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.registry;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityRegistry extends DeferredRegister<EntityType<?>> {

    private final Regilite regilite;

    protected EntityRegistry(Regilite regilite) {
        super(BuiltInRegistries.ENTITY_TYPE.key(), regilite.getModId());
        this.regilite = regilite;
    }

    private <T extends Entity> RegiliteEntity<T> registerEntity(String name, Function<ResourceLocation, EntityType<T>> func) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = ResourceLocation.fromNamespaceAndPath(getNamespace(), name);

        RegiliteEntity<T> ret = RegiliteEntity.createEntity(ResourceKey.create(getRegistryKey(), key), regilite);

        var entries = DeferredRegistryReflect.getEntries(this);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <T extends Entity> RegiliteEntity<T> registerEntity(String name, EntityType.EntityFactory<T> sup, MobCategory category) {
        return this.registerEntity(name, () -> EntityType.Builder.of(sup, category).build(name));
    }

    public <T extends Entity> RegiliteEntity<T> registerEntity(String name, Supplier<EntityType<T>> supplier) {
        return this.registerEntity(name, key -> supplier.get());
    }

    public static EntityRegistry create(Regilite regilite) {
        return new EntityRegistry(regilite);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        regilite.addEntities(this.getEntries());
    }
}
