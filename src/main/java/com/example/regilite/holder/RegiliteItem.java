package com.example.regilite.holder;

import com.example.regilite.data.RegiliteDataProvider;
import com.example.regilite.data.RegiliteItemModelProvider;
import com.example.regilite.events.IItemColor;
import com.example.regilite.registry.ITagagble;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegiliteItem<T extends Item> extends DeferredItem<T> implements ITagagble<Item> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    protected Set<TagKey<Item>> ItemTags = new HashSet<>();
    protected Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> tab = new HashMap<>();
    @Nullable
    protected BiConsumer<RegiliteItemModelProvider, T> modelProvider = RegiliteItemModelProvider::basicItem;
    protected IItemColor colorSupplier;

    protected RegiliteItem(ResourceKey<Item> key) {
        super(key);
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
    }

    public RegiliteItem<T> setTranslation(String translation) {
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }

    @SafeVarargs
    public final RegiliteItem<T> addItemTags(TagKey<Item>... tags) {
        ItemTags.addAll(Set.of(tags));
        return this;
    }

    public Set<TagKey<Item>> getTags() {
        return ItemTags;
    }

    public RegiliteItem<T> setTab(ResourceKey<CreativeModeTab> tab) {
        this.tab.put(tab, output -> output.accept(new ItemStack(this.get())));
        return this;
    }

    public RegiliteItem<T> setTab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        this.tab.put(tab, output -> output.accept(new ItemStack(this.get()), visibility));
        return this;
    }

    public RegiliteItem<T> setTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTab.Output> output) {
        this.tab.put(tab, output);
        return this;
    }

    public Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> getTab() {
        return tab;
    }

    public RegiliteItem<T> setModelProvider(BiConsumer<RegiliteItemModelProvider, T> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }

    public BiConsumer<RegiliteItemModelProvider, T> getModelProvider() {
        return modelProvider;
    }

    public IItemColor getColorSupplier() {
        return colorSupplier;
    }

    public RegiliteItem<T> setColorSupplier(IItemColor colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public static <T extends Item> RegiliteItem<T> createItem(ResourceLocation key) {
        return createItem(ResourceKey.create(Registries.ITEM, key));
    }

    /**
     * Creates a new {@link DeferredHolder} targeting the specified {@link Item}.
     *
     * @param <T> The type of the target {@link Item}.
     * @param key The resource key of the target {@link Item}.
     */
    public static <T extends Item> RegiliteItem<T> createItem(ResourceKey<Item> key) {
        return new RegiliteItem<>(key);
    }

    public static class RegiliteBucketItem<T extends BucketItem, U extends FluidType> extends RegiliteItem<T> {

        private final RegiliteFluid<U> fluid;

        protected RegiliteBucketItem(ResourceKey<Item> key, RegiliteFluid<U> fluid) {
            super(key);
            this.fluid = fluid;
            this.modelProvider = RegiliteItemModelProvider::bucketItem;
            setTranslation("");
        }

        public RegiliteFluid<U> finishBucket() {
            return fluid;
        }

        public static <I extends BucketItem, U extends FluidType> RegiliteBucketItem<I,U> createLiquidBlock(ResourceKey<Item> key, RegiliteFluid<U> fluid) {
            return new RegiliteBucketItem<>(key, fluid);
        }

        @Override
        public RegiliteBucketItem<T, U> setTab(ResourceKey<CreativeModeTab> tab) {
            super.setTab(tab);
            return this;
        }

        @Override
        public RegiliteBucketItem<T, U> setModelProvider(BiConsumer<RegiliteItemModelProvider, T> modelProvider) {
            super.setModelProvider(modelProvider);
            return this;
        }

        @SafeVarargs
        public final RegiliteBucketItem<T,U> addBucketItemTags(TagKey<Item>... tags) {
            super.addItemTags(tags);
            return this;
        }

        @Override
        public RegiliteBucketItem<T,U> setTranslation(String translation) {
            super.setTranslation(translation);
            return this;
        }
    }
}
