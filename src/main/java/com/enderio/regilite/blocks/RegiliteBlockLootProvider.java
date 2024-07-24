package com.enderio.regilite.blocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RegiliteBlockLootProvider extends BlockLootSubProvider {
    private final RegiliteBlocks blocks;

    public RegiliteBlockLootProvider(HolderLookup.Provider lookupProvider, RegiliteBlocks blocks) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        this.blocks = blocks;
    }

    @Override
    protected void generate() {
        blocks.blockBuilders().forEach(blockBuilder -> {
            if (blockBuilder.get().getLootTable() == BuiltInLootTables.EMPTY) {
                return;
            }

            var lootTable = blockBuilder.lootTable();
            if (lootTable != null) {
                processLootTable(lootTable, blockBuilder::get);
            }
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return blocks.blockBuilders().map(i -> (Block)i.get()).toList();
    }

    private <T extends Block> void processLootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable, Supplier<? extends Block> blockSupplier) {
        //noinspection unchecked
        lootTable.accept(this, (T)blockSupplier.get());
    }

    // region Helpers

    @Override
    public void dropSelf(Block block) {
        super.dropSelf(block);
    }

    public void createDoor(Block block) {
        this.add(block, super::createDoorTable);
    }

    @Override
    public void add(Block p_250610_, LootTable.Builder p_249817_) {
        super.add(p_250610_, p_249817_);
    }

    public void noDrop(Block block) {
        this.add(block, noDrop());
    }

    // endregion
}
