package com.example.examplemod.testmod;

import com.example.examplemod.registry.EnderBlockRegistry;
import com.example.examplemod.registry.EnderDeferredFluid;
import com.example.examplemod.registry.EnderFluidRegister;
import com.example.examplemod.registry.EnderItemRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Fluids {
    private static final EnderFluidRegister FLUIDTYPES = EnderFluidRegister.create(ExampleMod.MODID);
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID.key(),ExampleMod.MODID);
    public static final EnderBlockRegistry BLOCKS = EnderBlockRegistry.createRegistry(ExampleMod.MODID);
    public static final EnderItemRegistry ITEMS = EnderItemRegistry.createRegistry(ExampleMod.MODID);

    public static final EnderDeferredFluid<? extends FluidType> EXAMPLE_FLUID = FLUIDTYPES.registerFluid("example_fluid", FluidType.Properties.create())
            .createFluid(properties -> {})
            .withBlock(fluid -> new LiquidBlock(fluid, BlockBehaviour.Properties.copy(Blocks.WATER)))
            .finishLiquidBlock()
            .withBucket(fluid -> new BucketItem(fluid, new Item.Properties()))
            .finishBucket();

    public static void register(IEventBus modEventBus) {
        FLUIDTYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

    }
}
