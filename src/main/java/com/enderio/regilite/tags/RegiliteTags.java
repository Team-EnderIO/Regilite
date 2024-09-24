/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.tags;

import com.enderio.regilite.RegiliteModuleDataGen;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteTags implements RegiliteModuleDataGen {
    private final String modId;
    private final Object2ObjectMap<ResourceLocation, RegistryTagBuilder<?>> registries = new Object2ObjectOpenHashMap<>();

    public RegiliteTags(String modId) {
        this.modId = modId;
    }

    public RegistryTagBuilder<Block> blocks() {
        return registry(Registries.BLOCK, b -> b.builtInRegistryHolder().key());
    }

    public RegistryTagBuilder<BlockEntityType<?>> blockEntityTypes() {
        return registry(Registries.BLOCK_ENTITY_TYPE, b -> b.builtInRegistryHolder().key());
    }

    public RegistryTagBuilder<EntityType<?>> entityTypes() {
        return registry(Registries.ENTITY_TYPE, e -> e.builtInRegistryHolder().key());
    }

    public RegistryTagBuilder<Fluid> fluids() {
        return registry(Registries.FLUID, f -> f.builtInRegistryHolder().key());
    }

    public RegistryTagBuilder<Item> items() {
        return registry(Registries.ITEM, i -> i.builtInRegistryHolder().key());
    }

    public <T> RegistryTagBuilder<T> registry(ResourceKey<Registry<T>> registry, Function<T, ResourceKey<T>> keyExtractor) {
        //noinspection unchecked
        return (RegistryTagBuilder<T>)registries.computeIfAbsent(registry.location(), l -> new RegistryTagBuilder<>(registry, keyExtractor));
    }

    @Override
    public void gatherProviders(GatherDataEvent event, Consumer<DataProvider> addProvider) {
        if (!event.includeServer()) {
            return;
        }

        var packOutput = event.getGenerator().getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        for (var registry : registries.values()) {
            addProvider.accept(new TagProvider<>(packOutput, lookupProvider, modId,
                    existingFileHelper, registry));
        }
    }

    private class TagProvider<T> extends IntrinsicHolderTagsProvider<T> {

        private final RegistryTagBuilder<T> tagBuilder;

        public TagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper, RegistryTagBuilder<T> tagBuilder) {
            super(output, tagBuilder.registry(), lookupProvider, tagBuilder.keyExtractor, modId, existingFileHelper);
            this.tagBuilder = tagBuilder;
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            for (var pair : tagBuilder.tags.entrySet()) {
                //noinspection unchecked
                T[] items = (T[])pair.getValue().entries().map(Supplier::get).toArray();
                tag(pair.getKey()).add(items);
            }
        }
    }
}
