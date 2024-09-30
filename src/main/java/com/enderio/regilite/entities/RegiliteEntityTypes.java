/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.entities;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.RegiliteModuleEvents;
import com.enderio.regilite.RegiliteRegistryModule;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public class RegiliteEntityTypes implements RegiliteRegistryModule<EntityType<?>, DeferredRegister<EntityType<?>>>, RegiliteModuleEvents {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final DeferredRegister<EntityType<?>> deferredRegister;

    final ObjectList<EntityTypeBuilder<?>> entities = new ObjectArrayList<>();

    protected RegiliteEntityTypes(RegiliteLang langModule, RegiliteTags tagsModule, DeferredRegister<EntityType<?>> deferredRegister) {
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.deferredRegister = deferredRegister;
    }

    @ApiStatus.Internal
    public static RegiliteEntityTypes create(Regilite regilite) {
        return new RegiliteEntityTypes(regilite.lang(), regilite.tags(), DeferredRegister.create(Registries.ENTITY_TYPE, regilite.modId()));
    }

    public <T extends Entity> EntityTypeBuilder<T> create(String name, EntityType.EntityFactory<T> factory, MobCategory category) {
        return create(name, () -> EntityType.Builder.of(factory, category).build(name));
    }

    public <T extends Entity> EntityTypeBuilder<T> create(String name, Supplier<EntityType<T>> supplier) {
        var holder = deferredRegister.register(name, supplier);
        var builder = new EntityTypeBuilder<>(holder, langModule, tagsModule);
        entities.add(builder);
        return builder;
    }

    @Override
    public void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);
        modEventBus.addListener(this::onRegisterCapabilities);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(new RegiliteClientEntityTypes(this));
        }
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        entities.forEach(entityTypeBuilder -> entityTypeBuilder.attachCapabilities(event));
    }

    @Override
    public ResourceKey<Registry<EntityType<?>>> registry() {
        return Registries.ENTITY_TYPE;
    }

    @Override
    public DeferredRegister<EntityType<?>> deferredRegister() {
        return deferredRegister;
    }
}
