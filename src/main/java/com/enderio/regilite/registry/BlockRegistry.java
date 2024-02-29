package com.enderio.regilite.registry;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.holder.RegiliteFluid;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistry extends DeferredRegister.Blocks {

    private final Regilite regilite;

    protected BlockRegistry(Regilite regilite) {
        super(regilite.getModid());
        this.regilite = regilite;
    }

    /**
     * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
     *
     * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param func A factory for the new block. The factory should not cache the created block.
     * @return A {@link DeferredHolder} that will track updates from the registry for this block.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <B extends Block> RegiliteBlock<B> register(String name, Function<ResourceLocation, ? extends B> func) {
        return ((RegiliteBlock<B>) super.register(name, func));
    }

    /**
     * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
     *
     * @param name The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param sup  A factory for the new block. The factory should not cache the created block.
     * @return A {@link DeferredHolder} that will track updates from the registry for this block.
     */
    @Override
    public <B extends Block> RegiliteBlock<B> register(String name, Supplier<? extends B> sup) {
        return this.register(name, key -> sup.get());
    }

    /**
     * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
     *
     * @param name  The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param func  A factory for the new block. The factory should not cache the created block.
     * @param props The properties for the created block.
     * @return A {@link DeferredHolder} that will track updates from the registry for this block.
     * @see #registerBlock(String, BlockBehaviour.Properties)
     */
    public <B extends Block> RegiliteBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        return this.register(name, () -> func.apply(props));
    }

    /**
     * Adds a new block to the list of entries to be registered and returns a {@link DeferredHolder} that will be populated with the created block automatically.
     *
     * @param name  The new block's name. It will automatically have the {@linkplain #getNamespace() namespace} prefixed.
     * @param props The properties for the created block.
     * @return A {@link DeferredHolder} that will track updates from the registry for this block.
     * @see #registerBlock(String, Function, BlockBehaviour.Properties)
     */
    public RegiliteBlock<Block> registerBlock(String name, BlockBehaviour.Properties props) {
        return this.registerBlock(name, Block::new, props);
    }

    public <B extends LiquidBlock, U extends FluidType> RegiliteBlock.RegiliteLiquidBlock<B, U> registerLiquidBlock(String name, Function<ResourceLocation, ? extends B> func, RegiliteFluid<U> fluid) {
        //if (seenRegisterEvent)
        //    throw new IllegalStateException("Cannot register new entries to DeferredRegister after RegisterEvent has been fired.");
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        RegiliteBlock.RegiliteLiquidBlock<B, U> ret = createLiquidHolder(this.getRegistryKey(), key, fluid);

        var entries = DeferredRegistryReflect.getEntries(this);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <B extends LiquidBlock, U extends FluidType> RegiliteBlock.RegiliteLiquidBlock<B, U> registerLiquidBlock(String namespace, Supplier<? extends B> supplier, RegiliteFluid<U> fluid) {
        return this.registerLiquidBlock(namespace, key -> supplier.get(), fluid);
    }

    public <U extends FluidType> RegiliteBlock.RegiliteLiquidBlock<LiquidBlock, U> registerLiquidBlock(String name, BlockBehaviour.Properties props, Supplier<FlowingFluid> fluidSupp, RegiliteFluid<U> fluid) {
        return this.registerLiquidBlock(name, (rl) -> new LiquidBlock(fluidSupp, props), fluid);
    }

    @Override
    protected <I extends Block> DeferredBlock<I> createHolder(ResourceKey<? extends Registry<Block>> registryKey, ResourceLocation key) {
        return RegiliteBlock.createBlock(ResourceKey.create(registryKey, key), regilite);
    }

    private <B extends LiquidBlock, U extends FluidType> RegiliteBlock.RegiliteLiquidBlock<B, U> createLiquidHolder(ResourceKey<? extends Registry<Block>> registryKey, ResourceLocation key, RegiliteFluid<U> fluid) {
        return RegiliteBlock.RegiliteLiquidBlock.createLiquidBlock(ResourceKey.create(registryKey, key), fluid, regilite);
    }

    public static BlockRegistry create(Regilite regilite) {
        return new BlockRegistry(regilite);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        regilite.addBlocks(this.getEntries());
    }
}
