/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.blockentities;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.tags.RegiliteTags;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntityTypeBuilder<T extends BlockEntity>
        extends RegiliteBuilder<BlockEntityTypeBuilder<T>, BlockEntityType<?>, BlockEntityType<T>, DeferredBlockEntityType<T>> {

    private final RegiliteTags tagsModule;

    protected Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> rendererFactory;

    private final List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    protected BlockEntityTypeBuilder(DeferredBlockEntityType<T> holder, RegiliteTags tagsModule) {
        super(holder);
        this.tagsModule = tagsModule;
    }

    public BlockEntityTypeBuilder<T> tag(TagKey<BlockEntityType<?>> tag) {
        tagsModule.blockEntityTypes().tag(tag).add(this::get);
        return this;
    }

    @SafeVarargs
    public final BlockEntityTypeBuilder<T> tags(TagKey<BlockEntityType<?>>... tags) {
        tagsModule.blockEntityTypes().addToTags(this::get, tags);
        return this;
    }

    public BlockEntityTypeBuilder<T> renderer(Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> rendererFactory) {
        this.rendererFactory = rendererFactory;
        return this;
    }

    public <TCap, TContext> BlockEntityTypeBuilder<T> capability(BlockCapability<TCap, TContext> capability, ICapabilityProvider<? super T, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    void attachCapabilities(RegisterCapabilitiesEvent event) {
        for (var attachedCapability : attachedCapabilityList) {
            attachedCapability.registerProvider(event, get());
        }
    }

    protected record AttachedCapability<T extends BlockEntity, TCap, TContext>(
            BlockCapability<TCap, TContext> capability,
            ICapabilityProvider<? super T, TContext, TCap> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
            event.registerBlockEntity(capability, type, provider);
        }
    }
}
