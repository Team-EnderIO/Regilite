package com.example.regilite.registry;

import com.example.regilite.data.EnderDataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnderDeferredFluid<T extends FluidType> extends DeferredHolder<FluidType, T> implements ITagagble<Fluid> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private Set<TagKey<Fluid>> FluidTags = Set.of();
    private DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid;
    private DeferredHolder<Fluid, BaseFlowingFluid.Source> sourceFluid;
    private EnderDeferredBlock.EnderDeferredLiquidBlock<? extends LiquidBlock, T> block;
    private EnderDeferredItem.EnderDeferredBucketItem<? extends BucketItem, T> bucket;
    private final BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(this, this::getSource, this::getFlowing).block(this::getBlock).bucket(this::getBucket);

    protected EnderDeferredFluid(ResourceKey<FluidType> key) {
        super(key);
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
    }

    public static <I extends FluidType> EnderDeferredFluid<I> createHolder(ResourceKey<FluidType> fluidTypeResourceKey) {
        return new EnderDeferredFluid<>(fluidTypeResourceKey);
    }

    public EnderDeferredFluid<T> createFluid(DeferredRegister<Fluid> register,Consumer<BaseFlowingFluid.Properties> consumer) {
        consumer.accept(properties);
        this.flowingFluid = register.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties));
        this.sourceFluid = register.register("fluid_" + getId().getPath() + "_still", () -> new BaseFlowingFluid.Source(properties));
        return this;
    }
    public EnderDeferredFluid<T> createFluid(DeferredRegister<Fluid> register) {
        return this.createFluid(register, properties1 -> {});
    }



    public EnderDeferredBlock.EnderDeferredLiquidBlock<? extends LiquidBlock, T> withBlock(EnderBlockRegistry registry, Function<Supplier<BaseFlowingFluid.Flowing>, ? extends LiquidBlock> supplier) {
        this.block = registry.registerLiquidBlock(getId().getPath(), () -> supplier.apply(this.flowingFluid), this);
        return this.block;
    }

    public EnderDeferredItem.EnderDeferredBucketItem<? extends BucketItem, T> withBucket(EnderItemRegistry registry, Function<Supplier<BaseFlowingFluid.Source>, ? extends BucketItem> supplier) {
        this.bucket = registry.registerBucket(getId().getPath(), () -> supplier.apply(this.sourceFluid), this);
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
