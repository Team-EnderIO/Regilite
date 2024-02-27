package com.enderio.regilite.registry;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.events.BlockEntityRendererEvents;
import com.enderio.regilite.events.FluidRenderTypeEvents;
import com.enderio.regilite.holder.RegiliteFluid;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FluidRegister extends DeferredRegister<FluidType>{

    private final Regilite regilite;

    protected FluidRegister(Regilite regilite) {
        super(NeoForgeRegistries.FLUID_TYPES.key(), regilite.getModid());
        this.regilite = regilite;
    }

    public static FluidRegister create(Regilite regilite) {
        return new FluidRegister(regilite);
    }

    @Override
    public <I extends FluidType> RegiliteFluid<I> register(String name, Function<ResourceLocation, ? extends I> func) {
        return (RegiliteFluid<I>) super.register(name, func);
    }

    @Override
    public <I extends FluidType> RegiliteFluid<I> register(String name, Supplier<? extends I> sup) {
        return this.register(name, key -> sup.get());
    }

    public RegiliteFluid<FluidType> registerFluid(String name, FluidType.Properties type) {
        RegiliteFluid<FluidType> fluid = this.register(name, () -> new FluidType(type) {
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
        regilite.addFluids(this.getEntries());
    }

    @Override
    protected <I extends FluidType> RegiliteFluid<I> createHolder(ResourceKey<? extends Registry<FluidType>> registryKey, ResourceLocation key) {
        return RegiliteFluid.createHolder(ResourceKey.create(registryKey, key), regilite);
    }
}
