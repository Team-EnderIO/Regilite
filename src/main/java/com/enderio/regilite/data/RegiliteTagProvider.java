package com.enderio.regilite.data;

import com.enderio.regilite.holder.RegiliteFluid;
import com.enderio.regilite.registry.ITagagble;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RegiliteTagProvider<T> extends IntrinsicHolderTagsProvider<T> {
    private final List<DeferredHolder<T, ? extends T>> registered;

    public RegiliteTagProvider(PackOutput output, ResourceKey<? extends Registry<T>> key, Function<T, ResourceKey<T>> func, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper, List<DeferredHolder<T, ? extends T>> registered) {
        super(output, key, lookupProvider, func, modId, existingFileHelper);
        this.registered = registered;
    }

    @Override
    protected void addTags(HolderLookup.Provider p_256380_) {
        for (DeferredHolder<T, ? extends T> entry : registered) {
            if (entry instanceof ITagagble) {
                Set<TagKey<T>> tag = ((ITagagble<T>) entry).getTags();

                if (tag != null) {
                    tag.forEach(t -> tag(t).add(entry.get()));
                }
            }
        }
    }

    public static class FluidTagProvider extends IntrinsicHolderTagsProvider<Fluid> {

        public final List<DeferredHolder<FluidType, ? extends FluidType>> registered;

        public FluidTagProvider(PackOutput output, ResourceKey<Registry<Fluid>> key, Function<Fluid, ResourceKey<Fluid>> func, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper, List<DeferredHolder<FluidType, ? extends FluidType>> registered) {
            super(output, key, lookupProvider, func, modId, existingFileHelper);
            this.registered = registered;
        }

        @Override
        protected void addTags(HolderLookup.Provider p_256380_) {
            for (DeferredHolder<FluidType, ? extends FluidType> entry : registered) {
                if (entry instanceof RegiliteFluid<? extends FluidType> fluidtype) {
                    Set<TagKey<Fluid>> tag = ((ITagagble<Fluid>) entry).getTags();

                    if (tag != null) {
                        tag.forEach(t -> tag(t).add(fluidtype.getSource()));
                    }
                }
            }
        }
    }
}
