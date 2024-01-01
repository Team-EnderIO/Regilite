package com.example.examplemod.registry;

import com.example.examplemod.data.EnderDataProvider;
import com.example.examplemod.data.EnderItemModelProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
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
import java.util.function.Function;
import java.util.function.Supplier;

public class EnderDeferredFluid<T extends FluidType> extends DeferredHolder<FluidType, T> implements ITagagble<Fluid>{
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private Set<TagKey<Fluid>> FluidTags = Set.of();
    private DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid;
    private DeferredHolder<Fluid, BaseFlowingFluid.Source> sourceFluid;
    private EnderDeferredBlock.EnderDeferredLiquidBlock<? extends LiquidBlock> block;
    private EnderDeferredItem.EnderDeferredBucketItem<? extends Item> bucket;
    private final BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(this, this::getSource, this::getFlowing).block(this::getBlock).bucket(this::getBucket);
    private DeferredRegister<Fluid> fluid;
    private EnderBlockRegistry BLOCKS;
    private EnderItemRegistry ITEMS;

    protected EnderDeferredFluid(ResourceKey<FluidType> key) {
        super(key);
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
    }

    public static <I extends FluidType> EnderDeferredFluid<I> createHolder(ResourceKey<FluidType> fluidTypeResourceKey) {
        return new EnderDeferredFluid<>(fluidTypeResourceKey);
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

    public EnderDeferredBlock.EnderDeferredLiquidBlock<? extends LiquidBlock> withBlock(Function<Supplier<BaseFlowingFluid.Flowing>, ? extends LiquidBlock> supplier) {
        this.block = BLOCKS.registerLiquidBlock(getId().getNamespace(), () -> supplier.apply(this.flowingFluid)).setFluid(this);
        return this.block;
    }

    public EnderDeferredItem.EnderDeferredBucketItem<? extends BucketItem> withBucket(Function<Supplier<BaseFlowingFluid.Source>, ? extends BucketItem> supplier) {
        this.bucket = ITEMS.registerBucket(getId().getNamespace(), () -> supplier.apply(this.sourceFluid)).setFluid(this);
        return this.bucket;
    }

    public BaseFlowingFluid.Source getSource() {
        return sourceFluid.get();
    }

    public BaseFlowingFluid.Flowing getFlowing() {
        return flowingFluid.get();
    }

    public BucketItem getBucket() {
        return bucket.get();
    }

    public LiquidBlock getBlock() {
        return block.get();
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
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }
}
