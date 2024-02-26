package com.enderio.regilite.registry;

import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.data.RegiliteTagProvider;
import com.enderio.regilite.events.BlockEntityCapabilityEvents;
import com.enderio.regilite.events.BlockEntityRendererEvents;
import com.enderio.regilite.holder.RegiliteBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntityRegistry extends DeferredRegister<BlockEntityType<?>> {

    private static List<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> registered = new ArrayList<>();

    protected BlockEntityRegistry(String namespace) {
        super(BuiltInRegistries.BLOCK_ENTITY_TYPE.key(), namespace);
    }

    public <T extends BlockEntity> RegiliteBlockEntity<T> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> sup, Block... blocks) {
        return this.registerBlockEntity(name, () -> BlockEntityType.Builder.of(sup, blocks).build(null));
    }

    private <T extends BlockEntity> RegiliteBlockEntity<T> registerBlockEntity(String name, Function<ResourceLocation, BlockEntityType<T>> func) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        RegiliteBlockEntity<T> ret = RegiliteBlockEntity.createBlockEntity(ResourceKey.create(getRegistryKey(), key));

        var entries = DeferredRegistryReflect.getEntries(this);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    //TODO steam mess, can this be cleaner?
    @SafeVarargs
    public final <T extends BlockEntity> RegiliteBlockEntity<T> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> sup, Supplier<? extends Block>... blocks) {
        return this.registerBlockEntity(name, () -> BlockEntityType.Builder.of(sup, Arrays.stream(blocks).map(Supplier::get).toList().toArray(new Block[] {})).build(null));
    }

    public <T extends BlockEntity> RegiliteBlockEntity<T> registerBlockEntity(String name, Supplier<BlockEntityType<T>> supplier) {
        return this.registerBlockEntity(name, key -> supplier.get());
    }

    protected <T extends BlockEntity> RegiliteBlockEntity<T> createBlockEntityHolder(ResourceKey<? extends Registry<BlockEntityType<?>>> registryKey, ResourceLocation key) {
        return RegiliteBlockEntity.createBlockEntity(ResourceKey.create(registryKey, key));
    }

    public static <T extends BlockEntity> BlockEntityRegistry create(String modid) {
        return new BlockEntityRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        registered.addAll(this.getEntries());
    }

    public static List<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> getRegistered() {
        return registered;
    }
}
