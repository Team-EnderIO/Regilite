package com.example.examplemod.exampleclasses;

import com.example.examplemod.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockentity extends BlockEntity {
    public ExampleBlockentity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntities.EXAMPLE_BLOCKENTITY.get(), p_155229_, p_155230_);
    }
}
