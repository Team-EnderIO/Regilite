package com.enderio.regilite.tags;

import com.enderio.regilite.RegiliteDataModule;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteTags implements RegiliteDataModule {
    private final String modId;
    private final Object2ObjectMap<ResourceLocation, RegistryTagBuilder<?>> registries = new Object2ObjectOpenHashMap<>();

    public RegiliteTags(String modId) {
        this.modId = modId;
    }

    public RegistryTagBuilder<Block> blocks() {
        return registry(Registries.BLOCK, b -> b.builtInRegistryHolder().key());
    }

    public RegistryTagBuilder<Item> items() {
        return registry(Registries.ITEM, i -> i.builtInRegistryHolder().key());
    }

    public <T> RegistryTagBuilder<T> registry(ResourceKey<Registry<T>> registry, Function<T, ResourceKey<T>> keyExtractor) {
        //noinspection unchecked
        return (RegistryTagBuilder<T>)registries.computeIfAbsent(registry.location(), l -> new RegistryTagBuilder<>(registry, keyExtractor));
    }

    @Override
    public void addDataProviders(GatherDataEvent event, BiConsumer<Boolean, DataProvider> addProvider) {
        for (var registry : registries.values()) {
            addProvider.accept(event.includeServer(), new TagProvider<>(event.getGenerator().getPackOutput(),
                    event.getLookupProvider(), modId, event.getExistingFileHelper(), registry));
        }
    }

    private class TagProvider<T> extends IntrinsicHolderTagsProvider<T> {

        private final RegistryTagBuilder<T> tags;

        public TagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper, RegistryTagBuilder<T> tags) {
            super(output, tags.registry(), lookupProvider, tags::getKey, modId, existingFileHelper);
            this.tags = tags;
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            for (var pair : tags.entrySet()) {
                //noinspection unchecked
                T[] items = (T[])pair.getValue().entries().map(Supplier::get).toArray();
                tag(pair.getKey()).add(items);
            }
        }
    }
}
