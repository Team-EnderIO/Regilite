package com.enderio.regilite.data;

import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.registry.BlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.function.BiConsumer;

public class RegiliteBlockStateProvider extends BlockStateProvider {
    private final List<DeferredHolder<Block, ? extends Block>> registered;

    public RegiliteBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, List<DeferredHolder<Block, ? extends Block>> registered) {
        super(output, modid, exFileHelper);
        this.registered = registered;
    }

    @Override
    protected void registerStatesAndModels() {
        for (DeferredHolder<Block, ? extends Block> block : registered) {
            if (block instanceof RegiliteBlock) {
                BiConsumer<BlockStateProvider, DataGenContext<Block, Block>> blockstate = ((RegiliteBlock<Block>) block).getBlockStateProvider();
                if (blockstate != null) {
                    blockstate.accept(this, new DataGenContext<>(block.getKey().location(), block::get));
                }
            }
        }
    }
}
