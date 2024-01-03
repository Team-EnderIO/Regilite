package com.example.examplemod.registry;

import com.example.examplemod.data.EnderDataProvider;
import com.example.examplemod.data.EnderItemModelProvider;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.StringUtils;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnderDeferredItem<T extends Item> extends DeferredItem<T> implements ITagagble<Item>{
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    protected Set<TagKey<Item>> ItemTags = new HashSet<>();
    protected Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> tab = new HashMap<>();
    @Nullable
    protected BiConsumer<EnderItemModelProvider, T> modelProvider = EnderItemModelProvider::basicItem;
    protected Supplier<ItemColor> colorSupplier;

    protected EnderDeferredItem(ResourceKey<Item> key) {
        super(key);
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
    }

    public EnderDeferredItem<T> setTranslation(String translation) {
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }

    @SafeVarargs
    public final EnderDeferredItem<T> addItemTags(TagKey<Item>... tags) {
        ItemTags.addAll(Set.of(tags));
        return this;
    }

    public Set<TagKey<Item>> getTags() {
        return ItemTags;
    }

    public EnderDeferredItem<T> setTab(ResourceKey<CreativeModeTab> tab) {
        this.tab.put(tab, output -> output.accept(new ItemStack(this.get())));
        return this;
    }

    public EnderDeferredItem<T> setTab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        this.tab.put(tab, output -> output.accept(new ItemStack(this.get()), visibility));
        return this;
    }

    public EnderDeferredItem<T> setTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTab.Output> output) {
        this.tab.put(tab, output);
        return this;
    }

    public Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> getTab() {
        return tab;
    }

    public EnderDeferredItem<T> setModelProvider(BiConsumer<EnderItemModelProvider, T> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }

    public BiConsumer<EnderItemModelProvider, T> getModelProvider() {
        return modelProvider;
    }

    public Supplier<ItemColor> getColorSupplier() {
        return colorSupplier;
    }

    public EnderDeferredItem<T> setColorSupplier(Supplier<ItemColor> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public static <T extends Item> EnderDeferredItem<T> createItem(ResourceLocation key) {
        return createItem(ResourceKey.create(Registries.ITEM, key));
    }

    /**
     * Creates a new {@link DeferredHolder} targeting the specified {@link Item}.
     *
     * @param <T> The type of the target {@link Item}.
     * @param key The resource key of the target {@link Item}.
     */
    public static <T extends Item> EnderDeferredItem<T> createItem(ResourceKey<Item> key) {
        return new EnderDeferredItem<>(key);
    }

    public static class EnderDeferredBucketItem<T extends BucketItem, U extends FluidType> extends EnderDeferredItem<T> {

        private final EnderDeferredFluid<U> fluid;

        protected EnderDeferredBucketItem(ResourceKey<Item> key, EnderDeferredFluid<U> fluid) {
            super(key);
            this.fluid = fluid;
            this.modelProvider = EnderItemModelProvider::bucketItem;
            setTranslation("");
        }

        public EnderDeferredFluid<U> finishBucket() {
            return fluid;
        }

        public static <I extends BucketItem, U extends FluidType> EnderDeferredBucketItem<I,U> createLiquidBlock(ResourceKey<Item> key, EnderDeferredFluid<U> fluid) {
            return new EnderDeferredBucketItem<>(key, fluid);
        }


    }
}
