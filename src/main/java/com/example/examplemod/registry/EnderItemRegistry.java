package com.example.examplemod.registry;

import com.example.examplemod.data.*;
import com.example.examplemod.events.ColorEvents;
import com.example.examplemod.mixin.DeferredRegisterAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnderItemRegistry extends DeferredRegister.Items {
    protected EnderItemRegistry(String namespace) {
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
    public <I extends Item> EnderDeferredItem<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return (EnderDeferredItem<I>) super.register(name, func);
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
    public <I extends Item> EnderDeferredItem<I> register(String name, Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    private <I extends BlockItem, U extends Block> EnderDeferredBlockItem<I,U> registerBlockItem(String name, Function<ResourceLocation, I> func, EnderDeferredBlock<U> block) {
        //if (seenRegisterEvent)
        //throw new IllegalStateException("Cannot register new entries to DeferredRegister after RegisterEvent has been fired.");
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        EnderDeferredBlockItem<I, U> ret = createBlockItemHolder(getRegistryKey(), key, block);

        if (((DeferredRegisterAccessor<Item>)this).getEntries().putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <I extends BlockItem, U extends Block> EnderDeferredBlockItem<I, U> registerBlockItem(String name, EnderDeferredBlock<U> block, Supplier<I> sup) {
        return this.registerBlockItem(name, key -> sup.get(), block);
    }


    public <U extends Block> EnderDeferredBlockItem<BlockItem, U> registerBlockItem(String name, EnderDeferredBlock<U> block, Item.Properties properties) {
        return this.registerBlockItem(name, key -> new BlockItem(block.get(), properties), block);
    }

    public <U extends Block> EnderDeferredBlockItem<BlockItem, U> registerBlockItem(String name, EnderDeferredBlock<U> block) {
        return this.registerBlockItem(name, block, new Item.Properties());
    }

    public <U extends Block> EnderDeferredBlockItem<BlockItem, U> registerBlockItem(EnderDeferredBlock<U> block, Item.Properties properties) {
        return this.registerBlockItem(block.unwrapKey().orElseThrow().location().getPath(), block, properties);
    }

    public <U extends Block> EnderDeferredBlockItem<BlockItem, U> registerBlockItem(EnderDeferredBlock<U> block) {
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
    public <I extends Item> EnderDeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func, Item.Properties props) {
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
    public <I extends Item> EnderDeferredItem<I> registerItem(String name, Function<Item.Properties, ? extends I> func) {
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
    public EnderDeferredItem<Item> registerItem(String name, Item.Properties props) {
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
    public EnderDeferredItem<Item> registerItem(String name) {
        return this.registerItem(name, Item::new, new Item.Properties());
    }

    @Override
    protected <I extends Item> EnderDeferredItem<I> createHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key) {
        return EnderDeferredItem.createItem(ResourceKey.create(registryKey, key));
    }

    protected <I extends BlockItem, U extends Block> EnderDeferredBlockItem<I, U> createBlockItemHolder(ResourceKey<? extends Registry<Item>> registryKey, ResourceLocation key, EnderDeferredBlock<U> block) {
        return EnderDeferredBlockItem.createBlockItem(ResourceKey.create(registryKey, key), block);
    }

    public static EnderItemRegistry createRegistry(String modid) {
        return new EnderItemRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        this.onGatherData();
        bus.addListener(this::addCreative);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(new ColorEvents(null, this)::registerBlockColor);
        }

    }

    private void onGatherData() {
        EnderDataProvider provider = EnderDataProvider.getInstance(getNamespace());
        provider.addItems(this.getEntries());
        provider.addServerSubProvider((packOutput, existingFileHelper, lookup) -> new EnderTagProvider<>(packOutput, this.getRegistryKey(), b -> b.builtInRegistryHolder().key(), lookup, getNamespace(), existingFileHelper, this));
        provider.addServerSubProvider((packOutput, existingFileHelper, lookup) -> new EnderItemModelProvider(packOutput, getNamespace(), existingFileHelper, this));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        for (DeferredHolder<Item, ? extends Item> item : this.getEntries()) {
            Consumer<CreativeModeTab.Output> outputConsumer = ((EnderDeferredItem<Item>) item).getTab().get(event.getTabKey());
            if (outputConsumer != null) {
                outputConsumer.accept(event);
            }
        }
    }
}
