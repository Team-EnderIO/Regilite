/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.fluids;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.RegiliteModuleEvents;
import com.enderio.regilite.blocks.RegiliteBlocks;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegiliteFluidTypes implements RegiliteModuleEvents {

    private final DeferredRegister<FluidType> fluidTypeRegister;
    private final DeferredRegister<Fluid> fluidRegister;
    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final RegiliteItems itemsModule;
    private final RegiliteBlocks blocksModule;

    private final ObjectList<FluidTypeBuilder<?>> fluids = new ObjectArrayList<>();

    protected RegiliteFluidTypes(DeferredRegister<FluidType> fluidTypeRegister, DeferredRegister<Fluid> fluidRegister, RegiliteLang langModule, RegiliteTags tagsModule, RegiliteItems itemsModule, RegiliteBlocks blocksModule) {
        this.fluidTypeRegister = fluidTypeRegister;
        this.fluidRegister = fluidRegister;
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.itemsModule = itemsModule;
        this.blocksModule = blocksModule;
    }

    public static RegiliteFluidTypes create(Regilite regilite) {
        return new RegiliteFluidTypes(DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, regilite.modId()),
                DeferredRegister.create(Registries.FLUID, regilite.modId()), regilite.lang(),
                regilite.tags(), regilite.items(), regilite.blocks());
    }

    // TODO: more overloads.

    public FluidTypeBuilder<FluidType> create(String name, FluidType.Properties properties) {
        var holder = fluidTypeRegister.register(name, () -> new FluidType(properties));
        var customHolder = DeferredFluidType.from(holder);
        var builder = new FluidTypeBuilder<>(customHolder, fluidRegister, langModule, tagsModule, itemsModule, blocksModule);
        fluids.add(builder);
        return builder;
    }

    @Override
    public void register(IEventBus modEventBus) {
        fluidTypeRegister.register(modEventBus);
        fluidRegister.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(this::onRegisterClientExtensionsEvent);
        }
    }

    private void onRegisterClientExtensionsEvent(RegisterClientExtensionsEvent event) {
        // TODO: This needs to be made extensible by mods.
        for (var fluidType : fluids) {
            ResourceLocation id = fluidType.getId();
            event.registerFluidType(new IClientFluidTypeExtensions() {
                @Override
                public ResourceLocation getStillTexture() {
                    return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath() + "_still");
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath() + "_flowing");
                }
            }, fluidType.get());
        }
    }

    private void onClientSetupEvent(FMLClientSetupEvent event) {
        for (var fluidType : fluids) {
            var renderType = fluidType.renderTypeSupplier.get().get();
            if (renderType != null) {
                ItemBlockRenderTypes.setRenderLayer(fluidType.finish().flowingFluid(), renderType);
                ItemBlockRenderTypes.setRenderLayer(fluidType.finish().sourceFluid(), renderType);
            }
        }
    }
}
