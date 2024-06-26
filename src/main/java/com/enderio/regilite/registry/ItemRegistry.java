package com.enderio.regilite.registry;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.holder.RegiliteFluid;
import com.enderio.regilite.holder.RegiliteItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ItemRegistry extends DeferredRegister.Items {

    private final Regilite regilite;

    protected ItemRegistry(Regilite regilite) {
        super(regilite.getModid());
        this.regilite = regilite;
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

    private <I extends BlockItem, U extends Block> RegiliteItem<I> registerBlockItem(String name, Function<ResourceLocation, I> func, RegiliteBlock<U> block) {
        //if (seenRegisterEvent)
        //throw new IllegalStateException("Cannot register new entries to DeferredRegister after RegisterEvent has been fired.");
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = ResourceLocation.fromNamespaceAndPath(getNamespace(), name);

        RegiliteItem<I> ret = createHolder(getRegistryKey(), key);

        var entries = DeferredRegistryReflect.getEntries(this);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret
                .setTranslation("")
                .setModelProvider((prov, ctx) -> prov.basicBlock(ctx.get()));
    }

    public <I extends BlockItem, U extends Block> RegiliteItem<I> registerBlockItem(String name, RegiliteBlock<U> block, Supplier<I> sup) {
        return this.registerBlockItem(name, key -> sup.get(), block);
    }


    public <U extends Block> RegiliteItem<BlockItem> registerBlockItem(String name, RegiliteBlock<U> block, Item.Properties properties) {
        return this.registerBlockItem(name, key -> new BlockItem(block.get(), properties), block);
    }

    public <U extends Block> RegiliteItem<BlockItem> registerBlockItem(String name, RegiliteBlock<U> block) {
        return this.registerBlockItem(name, block, new Item.Properties());
    }

    public <U extends Block> RegiliteItem<BlockItem> registerBlockItem(RegiliteBlock<U> block, Item.Properties properties) {
        return this.registerBlockItem(block.unwrapKey().orElseThrow().location().getPath(), block, properties);
    }

    public <U extends Block> RegiliteItem<BlockItem> registerBlockItem(RegiliteBlock<U> block) {
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
        final ResourceLocation key = ResourceLocation.fromNamespaceAndPath(getNamespace(), name);

        RegiliteItem.RegiliteBucketItem<I,U> ret = createBucketHolder(getRegistryKey(), key, fluid);

        var entries = DeferredRegistryReflect.getEntries(this);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    @Override
    protected <I extends Item> RegiliteItem<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key) {
        return RegiliteItem.createItem(ResourceKey.create(registryKey, key), regilite);
    }

    protected <I extends BucketItem, U extends FluidType> RegiliteItem.RegiliteBucketItem<I, U> createBucketHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key, RegiliteFluid<U> fluid) {
        return RegiliteItem.RegiliteBucketItem.createLiquidBlock(ResourceKey.create(registryKey, key), fluid, regilite);
    }

    public static ItemRegistry create(Regilite regilite) {
        return new ItemRegistry(regilite);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        regilite.addItems(this.getEntries());
    }
}
