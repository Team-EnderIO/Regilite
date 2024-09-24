/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.fluids;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.blocks.BlockBuilder;
import com.enderio.regilite.blocks.RegiliteBlocks;
import com.enderio.regilite.items.ItemBuilder;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FluidTypeBuilder<T extends FluidType> extends RegiliteBuilder<FluidTypeBuilder<T>, FluidType, T, FluidTypeHolder<T>> {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final RegiliteItems itemsModule;
    private final RegiliteBlocks blocksModule;

    private BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(holder, holder::sourceFluid, holder::flowingFluid).block(holder::block).bucket(holder::bucket);

    protected Supplier<Supplier<RenderType>> renderTypeSupplier = () -> null;

    protected FluidTypeBuilder(FluidTypeHolder<T> holder, DeferredRegister<Fluid> fluidRegister, RegiliteLang langModule, RegiliteTags tagsModule, RegiliteItems itemsModule, RegiliteBlocks blocksModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.itemsModule = itemsModule;
        this.blocksModule = blocksModule;

        // TODO: Make the factories configurable (and add opt-out).
        holder.flowingFluidHolder(fluidRegister.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties)));
        holder.sourceFluidHolder(fluidRegister.register("fluid_" + getId().getPath() + "_source", () -> new BaseFlowingFluid.Source(properties)));
    }

    public FluidTypeBuilder<T> tag(TagKey<Fluid> tag) {
        tagsModule.fluids().tag(tag).add(holder::sourceFluid, holder::flowingFluid);
        return this;
    }

    @SafeVarargs
    public final FluidTypeBuilder<T> tags(TagKey<Fluid>... tags) {
        tagsModule.fluids().addToTags(holder::sourceFluid, tags);
        tagsModule.fluids().addToTags(holder::flowingFluid, tags);
        return this;
    }

    public FluidTypeBuilder<T> translation(String translation) {
        langModule.addFluid(this::get, translation);
        return this;
    }

    public FluidTypeBuilder<T> properties(UnaryOperator<BaseFlowingFluid.Properties> propertiesOperator) {
        properties = propertiesOperator.apply(properties);
        return this;
    }

    public FluidTypeBuilder<T> bucket() {
        return customBucket(f -> new BucketItem(f.get(), new Item.Properties().stacksTo(1)), i -> {
        });
    }

    public FluidTypeBuilder<T> bucket(Consumer<ItemBuilder<BucketItem>> itemConfigure) {
        return customBucket(f -> new BucketItem(f.get(), new Item.Properties().stacksTo(1)), itemConfigure);
    }

    public FluidTypeBuilder<T> bucket(Item.Properties properties) {
        return customBucket(f -> new BucketItem(f.get(), properties), i -> {
        });
    }

    public <I extends BucketItem> FluidTypeBuilder<T> customBucket(Function<Supplier<? extends FlowingFluid>, I> supplier) {
        return customBucket(supplier, i -> {
        });
    }

    public <I extends BucketItem> FluidTypeBuilder<T> customBucket(Function<Supplier<? extends FlowingFluid>, I> supplier, Consumer<ItemBuilder<I>> itemConfigure) {
        var bucket = itemsModule.create(getId().getPath() + "_bucket", () -> supplier.apply(holder::sourceFluid));
        bucket.modelProvider((prov, ctx) -> prov.bucketItem(ctx.get()));
        itemConfigure.accept(bucket);
        holder.bucketHolder(bucket.finish());
        return this;
    }

    public FluidTypeBuilder<T> block(BlockBehaviour.Properties properties) {
        return block(f -> new LiquidBlock(f.get(), properties), b -> {
        });
    }

    public FluidTypeBuilder<T> block(BlockBehaviour.Properties properties, Consumer<BlockBuilder<LiquidBlock>> blockConfigure) {
        return block(f -> new LiquidBlock(f.get(), properties), blockConfigure);
    }

    public <B extends LiquidBlock> FluidTypeBuilder<T> block(Function<Supplier<? extends BaseFlowingFluid>, B> supplier) {
        return block(supplier, b -> {
        });
    }

    public <B extends LiquidBlock> FluidTypeBuilder<T> block(Function<Supplier<? extends BaseFlowingFluid>, B> supplier, Consumer<BlockBuilder<B>> blockConfigure) {
        var block = blocksModule.create(getId().getPath(), () -> supplier.apply(holder::flowingFluid));
        blockConfigure.accept(block);
        holder.blockHolder(block.finish());
        return this;
    }

    public FluidTypeBuilder<T> renderType(Supplier<Supplier<RenderType>> renderTypeSupplier) {
        this.renderTypeSupplier = renderTypeSupplier;
        return this;
    }
}
