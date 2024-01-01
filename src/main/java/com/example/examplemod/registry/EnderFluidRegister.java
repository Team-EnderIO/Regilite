package com.example.examplemod.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class EnderFluidRegister extends DeferredRegister<FluidType>{
    private final DeferredRegister<Fluid> FLUID;
    private final EnderBlockRegistry BLOCKS;
    private final EnderItemRegistry ITEMS;


    protected EnderFluidRegister(String namespace) {
        super(NeoForgeRegistries.FLUID_TYPES.key(), namespace);
        FLUID = DeferredRegister.create(BuiltInRegistries.FLUID, namespace);
        BLOCKS = EnderBlockRegistry.createRegistry(namespace);
        ITEMS = EnderItemRegistry.createRegistry(namespace);

    }

    public static EnderFluidRegister create(String modid) {
        return new EnderFluidRegister(modid);
    }

    @Override
    public <I extends FluidType> EnderDeferredFluid<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return (EnderDeferredFluid<I>) super.register(name, func);
    }

    @Override
    public <I extends FluidType> EnderDeferredFluid<I> register(String name, Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    public EnderDeferredFluid<FluidType> registerFluid(String name, FluidType.Properties type) {
        EnderDeferredFluid<FluidType> fluid = this.register(name, () -> new FluidType(type));
        fluid.setRegistries(FLUID, BLOCKS, ITEMS);
        return fluid;
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        FLUID.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    @Override
    protected <I extends FluidType> EnderDeferredFluid<I> createHolder(ResourceKey<? extends Registry<FluidType>> registryKey, ResourceLocation key) {
        return EnderDeferredFluid.createHolder(ResourceKey.create(registryKey, key));
    }
}
