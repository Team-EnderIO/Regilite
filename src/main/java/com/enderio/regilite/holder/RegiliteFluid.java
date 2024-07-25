/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.holder;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.registry.ItemRegistry;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteFluid<T extends FluidType> extends DeferredHolder<FluidType, T> implements RegiliteHolder<RegiliteFluid<T>> {
    private final Supplier<String> descriptionIdSupplier = () -> get().getDescriptionId();
    private final Regilite regilite;
    private DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid;
    private DeferredHolder<Fluid, BaseFlowingFluid.Source> sourceFluid;
    //private RegiliteBlock<? extends LiquidBlock> block;
    private RegiliteItem<? extends BucketItem> bucket;
    private final BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(this, this::getSource, this::getFlowing).block(this::getBlock).bucket(this::getBucket);
    private Supplier<Supplier<RenderType>> renderTypeSupplier = () -> null;

    protected RegiliteFluid(ResourceKey<FluidType> key, Regilite regilite) {
        super(key);
        this.regilite = regilite;
        regilite.addTranslation(descriptionIdSupplier, DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public static <I extends FluidType> RegiliteFluid<I> createHolder(ResourceKey<FluidType> fluidTypeResourceKey, Regilite regilite) {
        return new RegiliteFluid<>(fluidTypeResourceKey, regilite);
    }

    public RegiliteFluid<T> createFluid(DeferredRegister<Fluid> register, Consumer<BaseFlowingFluid.Properties> consumer) {
        consumer.accept(properties);
        this.flowingFluid = register.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties));
        this.sourceFluid = register.register("fluid_" + getId().getPath() + "_still", () -> new BaseFlowingFluid.Source(properties));
        return this;
    }

    public RegiliteFluid<T> createFluid(DeferredRegister<Fluid> register) {
        return this.createFluid(register, properties1 -> {
        });
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
        //return block.get();
        return null;
    }

    // region Block

    /*public RegiliteFluid<T> withBlock(BlockRegistry registry, BlockBehaviour.Properties properties) {
        return withBlock(registry, f -> new LiquidBlock(f.get(), properties), b -> {
        });
    }

    public RegiliteFluid<T> withBlock(BlockRegistry registry, BlockBehaviour.Properties properties, Consumer<RegiliteBlock<? extends LiquidBlock>> blockConfigure) {
        return withBlock(registry, f -> new LiquidBlock(f.get(), properties), blockConfigure);
    }

    public RegiliteFluid<T> withBlock(BlockRegistry registry, Function<Supplier<BaseFlowingFluid.Flowing>, ? extends LiquidBlock> supplier) {
        return withBlock(registry, supplier, b -> {
        });
    }

    public RegiliteFluid<T> withBlock(BlockRegistry registry, Function<Supplier<BaseFlowingFluid.Flowing>, ? extends LiquidBlock> supplier, Consumer<RegiliteBlock<? extends LiquidBlock>> blockConfigure) {
        this.block = registry.register(getId().getPath(), () -> supplier.apply(this.flowingFluid));
        blockConfigure.accept(this.block);
        return this;
    }*/

    // endregion

    // region Bucket Item

    public RegiliteFluid<T> withBucket(ItemRegistry registry) {
        return withCustomBucket(registry, f -> new BucketItem(f.get(), new Item.Properties().stacksTo(1)), i -> {
        });
    }

    public RegiliteFluid<T> withBucket(ItemRegistry registry, Consumer<RegiliteItem<BucketItem>> itemConfigure) {
        return withCustomBucket(registry, f -> new BucketItem(f.get(), new Item.Properties().stacksTo(1)), itemConfigure);
    }

    public RegiliteFluid<T> withBucket(ItemRegistry registry, Item.Properties properties) {
        return withCustomBucket(registry, f -> new BucketItem(f.get(), properties), i -> {});
    }

    public <I extends BucketItem> RegiliteFluid<T> withCustomBucket(ItemRegistry registry, Function<Supplier<BaseFlowingFluid.Source>, I> supplier) {
        return withCustomBucket(registry, supplier, i -> {
        });
    }

    public <I extends BucketItem> RegiliteFluid<T> withCustomBucket(ItemRegistry registry, Function<Supplier<BaseFlowingFluid.Source>, I> supplier, Consumer<RegiliteItem<I>> itemConfigure) {
        var bucket = registry.register(getId().getPath() + "_bucket", () -> supplier.apply(this.sourceFluid));
        itemConfigure.accept(bucket);
        this.bucket = bucket;
        return this;
    }

    // endregion

    @SafeVarargs
    public final RegiliteFluid<T> withTags(TagKey<Fluid>... tags) {
        regilite.tags().fluids().addToTags(this::getSource, tags);
        return this;
    }

    public RegiliteFluid<T> withTranslation(String translation) {
        regilite.addTranslation(descriptionIdSupplier, translation);
        return this;
    }

    public Supplier<RenderType> getRenderType() {
        return renderTypeSupplier.get();
    }

    public RegiliteFluid<T> withRenderType(Supplier<Supplier<RenderType>> renderTypeSupplier) {
        this.renderTypeSupplier = renderTypeSupplier;
        return this;
    }
}
