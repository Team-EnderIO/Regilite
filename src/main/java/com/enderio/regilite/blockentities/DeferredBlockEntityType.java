package com.enderio.regilite.blockentities;

import com.enderio.regilite.fluids.DeferredFluidType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DeferredBlockEntityType<T extends BlockEntity> extends DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> {
    private DeferredBlockEntityType(ResourceKey<BlockEntityType<?>> key) {
        super(key);
    }

    public static <T extends BlockEntity> DeferredBlockEntityType<T> from(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> blockEntityTypeHolder) {
        return new DeferredBlockEntityType<>(blockEntityTypeHolder.getKey());
    }

    public T create(BlockPos pos, BlockState state) {
        return value().create(pos, state);
    }
}
