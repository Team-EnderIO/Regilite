package com.enderio.regilite.blockentities;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.RegiliteModuleEvents;
import com.enderio.regilite.RegiliteRegistryModule;
import com.enderio.regilite.tags.RegiliteTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.function.Supplier;

public class RegiliteBlockEntityTypes implements RegiliteRegistryModule<BlockEntityType<?>, DeferredRegister<BlockEntityType<?>>>, RegiliteModuleEvents {

    private final RegiliteTags tagsModule;
    private final DeferredRegister<BlockEntityType<?>> deferredRegister;

    final ObjectList<BlockEntityTypeBuilder<?>> blockEntities = new ObjectArrayList<>();

    protected RegiliteBlockEntityTypes(RegiliteTags tagsModule, DeferredRegister<BlockEntityType<?>> deferredRegister) {
        this.tagsModule = tagsModule;
        this.deferredRegister = deferredRegister;
    }

    @ApiStatus.Internal
    public static RegiliteBlockEntityTypes create(Regilite regilite) {
        return new RegiliteBlockEntityTypes(regilite.tags(), DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, regilite.modId()));
    }

    @SafeVarargs
    public final <T extends BlockEntity> BlockEntityTypeBuilder<T> create(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block>... blocks) {
        return create(name, () -> {
            var blocksArray = Arrays.stream(blocks).map(Supplier::get).toList().toArray(new Block[]{});
            return BlockEntityType.Builder.of(factory, blocksArray).build(null);
        });
    }

    public <T extends BlockEntity> BlockEntityTypeBuilder<T> create(String name, Supplier<BlockEntityType<T>> supplier) {
        var holder = DeferredBlockEntityType.from(deferredRegister.register(name, supplier));
        var builder = new BlockEntityTypeBuilder<>(holder, tagsModule);
        blockEntities.add(builder);
        return builder;
    }

    @Override
    public void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);
        modEventBus.addListener(this::onRegisterCapabilities);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(new RegiliteClientBlockEntityTypes(this));
        }
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        blockEntities.forEach(blockEntityTypeBuilder -> blockEntityTypeBuilder.attachCapabilities(event));
    }

    @Override
    public ResourceKey<Registry<BlockEntityType<?>>> registry() {
        return Registries.BLOCK_ENTITY_TYPE;
    }

    @Override
    public DeferredRegister<BlockEntityType<?>> deferredRegister() {
        return deferredRegister;
    }
}
