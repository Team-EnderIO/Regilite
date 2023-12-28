package com.example.examplemod.data;

import com.example.examplemod.registry.ITagagble;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class EnderTagProvider<T> extends IntrinsicHolderTagsProvider<T> {
    private final DeferredRegister<T> registry;

    public EnderTagProvider(PackOutput output, ResourceKey<? extends Registry<T>> key, Function<T, ResourceKey<T>> func, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper, DeferredRegister<T> registry) {
        super(output, key, lookupProvider, func, modId, existingFileHelper);
        this.registry = registry;
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        for (DeferredHolder<T, ? extends T> entry : registry.getEntries()) {
            Set<TagKey<T>> tag = ((ITagagble<T>) entry).getTags();
            tag.forEach(t -> tag(t).add(entry.get()));
        }
    }
}
