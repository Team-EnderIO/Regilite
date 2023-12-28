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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.StringUtils;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnderDeferredFluid<T extends FluidType> extends DeferredHolder<FluidType, T> implements ITranslatable, ITagagble<Fluid>{
    protected String translation = StringUtils.capitalize(getId().getPath().replace('_', ' '));
    private Set<TagKey<Fluid>> FluidTags = Set.of();
    private DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid;
    private DeferredHolder<Fluid, BaseFlowingFluid.Source> sourceFluid;
    private EnderDeferredBlock.EnderDeferredLiquidBlock<? extends LiquidBlock> block;
    private EnderDeferredItem.EnderDeferredBucketItem<? extends Item> bucket;
    private final BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(this, sourceFluid, flowingFluid).block(block).bucket(bucket);
    private DeferredRegister<Fluid> fluid;
    private EnderBlockRegistry BLOCKS;
    private EnderItemRegistry ITEMS;

    protected EnderDeferredFluid(ResourceKey<FluidType> key) {
        super(key);
    }

    public EnderDeferredFluid<T> createFluid(Consumer<BaseFlowingFluid.Properties> consumer, Supplier<? extends BucketItem> bucket, Supplier<? extends LiquidBlock> block) {
        this.block = BLOCKS.registerLiquidBlock(getId().getPath(), (r) -> block.get());
        this.bucket = ITEMS.registerBucket(getId().getPath(), bucket);
        consumer.accept(properties);
        this.flowingFluid = fluid.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties));
        this.sourceFluid = fluid.register("fluid_" + getId().getPath() + "_still", () -> new BaseFlowingFluid.Source(properties));
        return this;
    }

    public EnderDeferredFluid<T> createSimpleFluid(Consumer<BaseFlowingFluid.Properties> consumer) {
        return this.createFluid(consumer, () -> new BucketItem(this.sourceFluid, new Item.Properties().stacksTo(1)),
            () -> new LiquidBlock(this.flowingFluid, BlockBehaviour.Properties.copy(Blocks.WATER)));
    }

    public EnderDeferredFluid<T> createFluid(Consumer<BaseFlowingFluid.Properties> consumer) {
        consumer.accept(properties);
        this.flowingFluid = fluid.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties));
        this.sourceFluid = fluid.register("fluid_" + getId().getPath() + "_still", () -> new BaseFlowingFluid.Source(properties));
        return this;
    }

    public EnderDeferredBlock.EnderDeferredLiquidBlock<? extends LiquidBlock> withBlock(Supplier<? extends LiquidBlock> supplier) {
        this.block = BLOCKS.registerLiquidBlock(getId().getNamespace(), supplier).setFluid(this);
        return this.block;
    }

    public EnderDeferredItem.EnderDeferredBucketItem<? extends BucketItem> withBucket(Supplier<? extends BucketItem> supplier) {
        this.bucket = ITEMS.registerBucket(getId().getNamespace(), supplier).setFluid(this);
        return this.bucket;
    }

    public BaseFlowingFluid.Source getSource() {
        return sourceFluid.get();
    }

    public BaseFlowingFluid.Flowing getFlowing() {
        return flowingFluid.get();
    }

    public void setRegistries(DeferredRegister<Fluid> fluid, EnderBlockRegistry blocks, EnderItemRegistry items) {
        this.fluid = fluid;
        this.BLOCKS = blocks;
        this.ITEMS = items;
    }

    @SafeVarargs
    public final EnderDeferredFluid<T> addFluidTags(TagKey<Fluid>... tags) {
        this.FluidTags = Set.of(tags);
        return this;
    }

    public Set<TagKey<Fluid>> getTags() {
        return FluidTags;
    }

    public EnderDeferredFluid<T> setTranslation(String translation) {
        this.translation = translation;
        return this;
    }

    @Override
    public Pair<String, String> getTranslation() {
        return new Pair<>(this.get().getDescriptionId(), translation);
    }
}
