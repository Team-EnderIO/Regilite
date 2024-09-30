/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.fluids.DeferredFluidType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.FluidType;

public class Fluids {
    public static final DeferredFluidType<FluidType> EXAMPLE_FLUID = ExampleMod.REGILITE.fluidTypes()
            .create("example_fluid", FluidType.Properties.create())
            .block(BlockBehaviour.Properties.ofFullCopy(Blocks.WATER))
            .bucket()
            .renderType(() -> RenderType::translucent)
            .finish();

    public static void register() {
    }
}
