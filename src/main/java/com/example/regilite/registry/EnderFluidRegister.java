package com.example.regilite.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnderFluidRegister extends DeferredRegister<FluidType>{

    protected EnderFluidRegister(String namespace) {
        super(NeoForgeRegistries.FLUID_TYPES.key(), namespace);
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
        EnderDeferredFluid<FluidType> fluid = this.register(name, () -> new FluidType(type) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public ResourceLocation getStillTexture() {
                        return new ResourceLocation(getNamespace(), "block/" + name + "_still");
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return new ResourceLocation(getNamespace(), "block/" + name + "_flowing");
                    }
                });
            }
        });
        return fluid;
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
    }

    @Override
    protected <I extends FluidType> EnderDeferredFluid<I> createHolder(ResourceKey<? extends Registry<FluidType>> registryKey, ResourceLocation key) {
        return EnderDeferredFluid.createHolder(ResourceKey.create(registryKey, key));
    }
}