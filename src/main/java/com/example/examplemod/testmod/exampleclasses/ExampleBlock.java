package com.example.examplemod.testmod.exampleclasses;

import com.example.examplemod.testmod.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExampleBlock extends Block implements EntityBlock {
    public ExampleBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntities.EXAMPLE_BLOCKENTITY.create(p_153215_, p_153216_);
    }
}
