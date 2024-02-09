package com.enderio.regilite.examplemod.exampleclasses;

import com.enderio.regilite.examplemod.BlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExampleBlockentity extends BlockEntity implements MenuProvider {
    public ExampleBlockentity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntities.EXAMPLE_BLOCKENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("test");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ExampleMenu(this, p_39954_, p_39955_);
    }
}
