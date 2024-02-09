package com.enderio.regilite.data;

import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.registry.BlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.BiConsumer;

public class RegiliteBlockStateProvider extends BlockStateProvider {
    private final BlockRegistry registry;

    public RegiliteBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, BlockRegistry registry) {
        super(output, modid, exFileHelper);
        this.registry = registry;
    }

    @Override
    protected void registerStatesAndModels() {
        for (DeferredHolder<Block, ? extends Block> block : registry.getEntries()) {
            if (block instanceof RegiliteBlock) {
                BiConsumer<BlockStateProvider, Block> blockstate = ((RegiliteBlock<Block>) block).getBlockStateProvider();
                if (blockstate != null) {
                    blockstate.accept(this, block.get());
                }
            }
        }
    }
}
