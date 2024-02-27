package com.enderio.regilite.examplemod;

import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.holder.RegiliteFluid;
import com.enderio.regilite.registry.FluidRegister;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.client.renderer.RenderType;
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
    private static final FluidRegister FLUIDTYPES = FluidRegister.create(ExampleMod.getRegilite());
    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID.key(), ExampleMod.MODID);
    public static final BlockRegistry BLOCKS = BlockRegistry.createRegistry(ExampleMod.getRegilite());
    public static final ItemRegistry ITEMS = ItemRegistry.createRegistry(ExampleMod.getRegilite());

    public static final RegiliteFluid<FluidType> EXAMPLE_FLUID = FLUIDTYPES.registerFluid("example_fluid", FluidType.Properties.create())
            .createFluid(FLUIDS)
            .withBlock(BLOCKS, fluid -> new LiquidBlock(fluid, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)))
            .finishLiquidBlock()
            .withBucket(ITEMS, fluid -> new BucketItem(fluid, new Item.Properties().stacksTo(1)))
            .finishBucket()
            .setRenderType(() -> RenderType::translucent);

    public static void register(IEventBus modEventBus) {
        FLUIDTYPES.register(modEventBus);
        FLUIDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
