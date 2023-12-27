package com.example.examplemod.registry;

import com.example.examplemod.data.EnderItemModelProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnderDeferredFluid<T extends FluidType> extends EnderDeferredObject<FluidType, T>{
    private Set<TagKey<Fluid>> FluidTags = Set.of();
    protected Set<TagKey<Item>> ItemTags = Set.of();
    protected ResourceKey<CreativeModeTab> tab;
    @Nullable
    protected BiConsumer<EnderItemModelProvider, ? extends BucketItem> modelProvider = EnderItemModelProvider::basicItem;
    private EnderDeferredObject<Fluid, BaseFlowingFluid.Flowing> flowingFluid;
    private EnderDeferredObject<Fluid, BaseFlowingFluid.Source> sourceFluid;
    private DeferredBlock<? extends LiquidBlock> block;
    private DeferredItem<? extends Item> bucket;
    private final BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(this, sourceFluid, flowingFluid).block(block).bucket(bucket);
    private EnderRegistry<Fluid> fluid;
    private DeferredRegister.Blocks BLOCKS;
    private DeferredRegister.Items ITEMS;

    protected EnderDeferredFluid(ResourceKey<FluidType> key) {
        super(key);
    }

    public EnderDeferredFluid<T> createFluids(Consumer<BaseFlowingFluid.Properties> consumer, Supplier<? extends Item> bucket, Supplier<? extends LiquidBlock> block) {
        this.block = BLOCKS.register(getId().getPath(), block);
        this.bucket = ITEMS.register(getId().getPath(), bucket);
        consumer.accept(properties);
        this.flowingFluid = fluid.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties));
        this.sourceFluid = fluid.register("fluid_" + getId().getPath() + "_still", () -> new BaseFlowingFluid.Source(properties));
        return this;
    }

    public EnderDeferredFluid<T> createFluids(Consumer<BaseFlowingFluid.Properties> consumer) {
        return this.createFluids(consumer, () -> new BucketItem(this.sourceFluid, new Item.Properties().stacksTo(1)),
            () -> new LiquidBlock(this.flowingFluid, BlockBehaviour.Properties.copy(Blocks.WATER)));
    }

    public BaseFlowingFluid.Source getSource() {
        return sourceFluid.get();
    }

    public BaseFlowingFluid.Flowing getFlowing() {
        return flowingFluid.get();
    }

    public void setRegistries(EnderRegistry<Fluid> fluid, DeferredRegister.Blocks blocks, DeferredRegister.Items items) {
        this.fluid = fluid;
        this.BLOCKS = blocks;
        this.ITEMS = items;
    }

    @SafeVarargs
    public final EnderDeferredFluid<T> addItemTags(TagKey<Item>... tags) {
        ItemTags = Set.of(tags);
        return this;
    }

    public Set<TagKey<Item>> getItemTags() {
        return ItemTags;
    }

    @SafeVarargs
    public final EnderDeferredFluid<T> addFluidTags(TagKey<Fluid>... tags) {
        this.FluidTags = Set.of(tags);
        return this;
    }

    public Set<TagKey<Fluid>> getFluidTags() {
        return FluidTags;
    }

    public EnderDeferredFluid<T> setTab(ResourceKey<CreativeModeTab> tab) {
        this.tab = tab;
        return this;
    }

    public ResourceKey<CreativeModeTab> getTab() {
        return tab;
    }

    public EnderDeferredFluid<T> setModelProvider(BiConsumer<EnderItemModelProvider, BucketItem> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }

    public BiConsumer<EnderItemModelProvider, ? extends BucketItem> getModelProvider() {
        return modelProvider;
    }

    @Override
    public EnderDeferredFluid<T> setTranslation(String translation) {
        super.setTranslation(translation);
        return this;
    }
}
