package com.enderio.regilite.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public interface IBlockColor {
    int getColor(BlockState state, @Nullable BlockAndTintGetter getter, @Nullable BlockPos pos, int layer);
}
