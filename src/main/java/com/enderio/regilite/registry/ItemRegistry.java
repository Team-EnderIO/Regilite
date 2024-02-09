package com.enderio.regilite.registry;

import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.data.RegiliteItemModelProvider;
import com.enderio.regilite.data.RegiliteTagProvider;
import com.enderio.regilite.events.ColorEvents;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.holder.RegiliteBlockItem;
import com.enderio.regilite.holder.RegiliteFluid;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.mixin.DeferredRegisterAccessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemRegistry extends DeferredRegister.Items {
    protected ItemRegistry(String namespace) {
        super(namespace);
    }

    /**
     * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
     *
     * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param func A factory for the new item. The factory should not cache the created item.
     * @return A {@link DeferredItem} that will track updates from the registry for this item.
     * @see #register(String, Supplier)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <I extends Item> RegiliteItem<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return (RegiliteItem<I>) super.register(name, func);
    }

    /**
     * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
     *
     * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param sup  A factory for the new item. The factory should not cache the created item.
     * @return A {@link DeferredItem} that will track updates from the registry for this item.
     * @see #register(String, Function)
     */
    @Override
    public <I extends Item> RegiliteItem<I> register(String name, Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    private <I extends BlockItem, U extends Block> RegiliteBlockItem<I,U> registerBlockItem(String name, Function<ResourceLocation, I> func, RegiliteBlock<U> block) {
        //if (seenRegisterEvent)
        //throw new IllegalStateException("Cannot register new entries to DeferredRegister after RegisterEvent has been fired.");
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        RegiliteBlockItem<I, U> ret = createBlockItemHolder(getRegistryKey(), key, block);

        if (((DeferredRegisterAccessor<Item>)this).getEntries().putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <I extends BlockItem, U extends Block> RegiliteBlockItem<I, U> registerBlockItem(String name, RegiliteBlock<U> block, Supplier<I> sup) {
        return this.registerBlockItem(name, key -> sup.get(), block);
    }


    public <U extends Block> RegiliteBlockItem<BlockItem, U> registerBlockItem(String name, RegiliteBlock<U> block, Item.Properties properties) {
        return this.registerBlockItem(name, key -> new BlockItem(block.get(), properties), block);
    }

    public <U extends Block> RegiliteBlockItem<BlockItem, U> registerBlockItem(String name, RegiliteBlock<U> block) {
        return this.registerBlockItem(name, block, new Item.Properties());
    }

    public <U extends Block> RegiliteBlockItem<BlockItem, U> registerBlockItem(RegiliteBlock<U> block, Item.Properties properties) {
        return this.registerBlockItem(block.unwrapKey().orElseThrow().location().getPath(), block, properties);
    }

    public <U extends Block> RegiliteBlockItem<BlockItem, U> registerBlockItem(RegiliteBlock<U> block) {
        return this.registerBlockItem(block, new Item.Properties());
    }

    /**
     * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
     *
     * @param name  The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param func  A factory for the new item. The factory should not cache the created item.
     * @param props The properties for the created item.
     * @return A {@link DeferredItem} that will track updates from the registry for this item.
     * @see #registerItem(String, Function)
     * @see #registerItem(String, Item.Properties)
     * @see #registerItem(String)
     */
    public <I extends Item> RegiliteItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func, Item.Properties props) {
        return this.register(name, () -> func.apply(props));
    }

    /**
     * Adds a new item to the list of entries to be registered and returns a {@link DeferredItem} that will be populated with the created item automatically.
     * This method uses the default {@link Item.Properties}.
     *
     * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param func A factory for the new item. The factory should not cache the created item.
     * @return A {@link DeferredItem} that will track updates from the registry for this item.
     * @see #registerItem(String, Function, Item.Properties)
     * @see #registerItem(String, Item.Properties)
     * @see #registerItem(String)
     */
    public <I extends Item> RegiliteItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func) {
        return this.registerItem(name, func, new Item.Properties());
    }

    /**
     * Adds a new {@link Item} with the given {@link Item.Properties properties} to the list of entries to be registered and
     * returns a {@link DeferredItem} that will be populated with the created item automatically.
     *
     * @param name  The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param props A factory for the new item. The factory should not cache the created item.
     * @return A {@link DeferredItem} that will track updates from the registry for this item.
     * @see #registerItem(String, Function, Item.Properties)
     * @see #registerItem(String, Function)
     * @see #registerItem(String)
     */
    public RegiliteItem<Item> registerItem(String name, Item.Properties props) {
        return this.registerItem(name, Item::new, props);
    }

    /**
     * Adds a new {@link Item} with the default {@link Item.Properties properties} to the list of entries to be registered and
     * returns a {@link DeferredItem} that will be populated with the created item automatically.
     *
     * @param name The new item's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @return A {@link DeferredItem} that will track updates from the registry for this item.
     * @see #registerItem(String, Function, Item.Properties)
     * @see #registerItem(String, Function)
     * @see #registerItem(String, Item.Properties)
     */
    public RegiliteItem<Item> registerItem(String name) {
        return this.registerItem(name, Item::new, new Item.Properties());
    }

    public <I extends BucketItem, U extends FluidType> RegiliteItem.RegiliteBucketItem<I, U> registerBucket(String name, Supplier<? extends I> supp, RegiliteFluid<U> fluid) {
        return this.registerBucket(name, key -> supp.get(), fluid);
    }

    public <I extends BucketItem, U extends FluidType> RegiliteItem.RegiliteBucketItem<I,U> registerBucket(String name, Function<ResourceLocation, ? extends I> func, RegiliteFluid<U> fluid) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        RegiliteItem.RegiliteBucketItem<I,U> ret = createBucketHolder(getRegistryKey(), key, fluid);

        if (((DeferredRegisterAccessor<Item>)this).getEntries().putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    @Override
    protected <I extends Item> RegiliteItem<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key) {
        return RegiliteItem.createItem(ResourceKey.create(registryKey, key));
    }

    protected <I extends BlockItem, U extends Block> RegiliteBlockItem<I, U> createBlockItemHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key, RegiliteBlock<U> block) {
        return RegiliteBlockItem.createBlockItem(ResourceKey.create(registryKey, key), block);
    }

    protected <I extends BucketItem, U extends FluidType> RegiliteItem.RegiliteBucketItem<I, U> createBucketHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key, RegiliteFluid<U> fluid) {
        return RegiliteItem.RegiliteBucketItem.createLiquidBlock(ResourceKey.create(registryKey, key), fluid);
    }

    public static ItemRegistry createRegistry(String modid) {
        return new ItemRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        this.onGatherData(bus);
        bus.addListener(this::addCreative);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(new ColorEvents.Items(this)::registerItemColor);
        }

    }

    private void onGatherData(IEventBus bus) {
        RegiliteDataProvider provider = RegiliteDataProvider.register(getNamespace(), bus);
        provider.addServerSubProvider((packOutput, existingFileHelper, lookup) -> new RegiliteTagProvider<>(packOutput, this.getRegistryKey(), b -> b.builtInRegistryHolder().key(), lookup, getNamespace(), existingFileHelper, this));
        provider.addServerSubProvider((packOutput, existingFileHelper, lookup) -> new RegiliteItemModelProvider(packOutput, getNamespace(), existingFileHelper, this));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        for (DeferredHolder<Item, ? extends Item> item : this.getEntries()) {
            if (item instanceof RegiliteItem) {
                Consumer<CreativeModeTab.Output> outputConsumer = ((RegiliteItem<Item>) item).getTab().get(event.getTabKey());
                if (outputConsumer != null) {
                    outputConsumer.accept(event);
                }
            }
        }
    }
}
