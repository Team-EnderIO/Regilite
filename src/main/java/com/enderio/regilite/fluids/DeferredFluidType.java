/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.fluids;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

public class DeferredFluidType<T extends FluidType> extends DeferredHolder<FluidType, T> {
    @Nullable
    private DeferredHolder<Fluid, ? extends BaseFlowingFluid> flowingFluidHolder;
    @Nullable
    private DeferredHolder<Fluid, ? extends BaseFlowingFluid> sourceFluidHolder;

    @Nullable
    private DeferredBlock<? extends LiquidBlock> blockHolder;
    @Nullable
    private DeferredItem<? extends BucketItem> bucketHolder;

    protected DeferredFluidType(ResourceKey<FluidType> key) {
        super(key);
    }

    static <T extends FluidType> DeferredFluidType<T> from(DeferredHolder<FluidType, T> fluidTypeHolder) {
        return new DeferredFluidType<>(fluidTypeHolder.getKey());
    }

    public DeferredHolder<Fluid, ? extends BaseFlowingFluid> flowingFluidHolder() {
        return flowingFluidHolder;
    }

    void flowingFluidHolder(DeferredHolder<Fluid, ? extends BaseFlowingFluid> flowingFluidHolder) {
        this.flowingFluidHolder = flowingFluidHolder;
    }

    public BaseFlowingFluid flowingFluid() {
        if (flowingFluidHolder == null) {
            return null;
        }

        return flowingFluidHolder.get();
    }

    public DeferredHolder<Fluid, ? extends BaseFlowingFluid> sourceFluidHolder() {
        return sourceFluidHolder;
    }

    void sourceFluidHolder(DeferredHolder<Fluid, ? extends BaseFlowingFluid> sourceFluidHolder) {
        this.sourceFluidHolder = sourceFluidHolder;
    }

    public BaseFlowingFluid sourceFluid() {
        if (sourceFluidHolder == null) {
            return null;
        }

        return sourceFluidHolder.get();
    }

    public DeferredBlock<? extends LiquidBlock> blockHolder() {
        return blockHolder;
    }

    void blockHolder(DeferredBlock<? extends LiquidBlock> blockHolder) {
        this.blockHolder = blockHolder;
    }

    public LiquidBlock block() {
        if (blockHolder == null) {
            return null;
        }

        return blockHolder.get();
    }

    public DeferredItem<? extends BucketItem> bucketHolder() {
        return bucketHolder;
    }

    void bucketHolder(DeferredItem<? extends BucketItem> bucketHolder) {
        this.bucketHolder = bucketHolder;
    }

    public BucketItem bucket() {
        if (bucketHolder == null) {
            return null;
        }

        return bucketHolder.get();
    }
}
