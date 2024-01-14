package com.example.regilite.data;

import com.example.regilite.registry.EnderBlockRegistry;
import com.example.regilite.registry.EnderDeferredBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.BiConsumer;

public class EnderBlockStateProvider extends BlockStateProvider {
    private final EnderBlockRegistry registry;

    public EnderBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, EnderBlockRegistry registry) {
        super(output, modid, exFileHelper);
        this.registry = registry;
    }

    @Override
    protected void registerStatesAndModels() {
        for (DeferredHolder<Block, ? extends Block> block : registry.getEntries()) {
            if (block instanceof EnderDeferredBlock) {
                BiConsumer<BlockStateProvider, Block> blockstate = ((EnderDeferredBlock<Block>) block).getBlockStateProvider();
                if (blockstate != null) {
                    blockstate.accept(this, block.get());
                }
            }
        }
    }
}
