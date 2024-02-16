package com.enderio.regilite.holder;

import com.enderio.regilite.data.DataGenContext;
import com.enderio.regilite.registry.ITagagble;
import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.data.RegiliteItemModelProvider;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegiliteItem<T extends Item> extends DeferredItem<T> implements ITagagble<Item> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    protected Set<TagKey<Item>> ItemTags = new HashSet<>();
    protected Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> tab = new HashMap<>();
    @Nullable
    protected BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider = (prov, ctx) -> prov.basicItem(ctx.get());
    protected Supplier<Supplier<ItemColor>> colorSupplier;
    protected List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    protected RegiliteItem(ResourceKey<Item> key) {
        super(key);
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public RegiliteItem<T> setTranslation(String translation) {
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }

    @SafeVarargs
    public final RegiliteItem<T> addItemTags(TagKey<Item>... tags) {
        ItemTags.addAll(new HashSet<>(List.of(tags)));
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

    public RegiliteItem<T> setModelProvider(BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }

    public BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> getModelProvider() {
        return modelProvider;
    }

    public Supplier<Supplier<ItemColor>> getColorSupplier() {
        return colorSupplier;
    }

    public RegiliteItem<T> setColorSupplier(Supplier<Supplier<ItemColor>> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public <TCap, TContext> RegiliteItem<T> addCapability(ItemCapability<TCap, TContext> capability, ICapabilityProvider<ItemStack, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    // Allows wrapping common holder builder methods into other methods and applying them.
    public RegiliteItem<T> apply(Consumer<RegiliteItem<T>> applicator) {
        applicator.accept(this);
        return this;
    }

    @ApiStatus.Internal
    public void registerCapabilityProviders(RegisterCapabilitiesEvent event) {
        for (AttachedCapability<T, ?, ?> capabilityProvider : attachedCapabilityList) {
            capabilityProvider.registerProvider(event, value());
        }
    }

    protected record AttachedCapability<T extends Item, TCap, TContext>(
            ItemCapability<TCap, TContext> capability,
            ICapabilityProvider<ItemStack, TContext, TCap> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, Item item) {
            event.registerItem(capability, provider, item);
        }
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
            this.modelProvider = (prov, ctx) -> prov.bucketItem(ctx.get());
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
        public RegiliteBucketItem<T, U> setModelProvider(BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider) {
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
