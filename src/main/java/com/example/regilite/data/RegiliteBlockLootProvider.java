package com.example.regilite.data;

import com.example.regilite.registry.BlockRegistry;
import com.example.regilite.holder.RegiliteBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;

public class RegiliteBlockLootProvider extends BlockLootSubProvider {

    private final BlockRegistry registry;

    public RegiliteBlockLootProvider(Set<Item> explosionResistant, BlockRegistry registry) {
        super(explosionResistant, FeatureFlags.REGISTRY.allFlags());
        this.registry = registry;
    }

    @Override
    protected void generate() {
        for (DeferredHolder<Block, ? extends Block> block : registry.getEntries()) {
            if (block instanceof RegiliteBlock) {
                BiConsumer<RegiliteBlockLootProvider, Block> lootTable = ((RegiliteBlock<Block>) block).getLootTable();
                if (lootTable != null) {
                    lootTable.accept(this, block.get());
                }
            }
        }
    }

    //TODO why these 2 methods, can we join them?
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> p_249322_) {
        this.generate();
        Set<ResourceLocation> set = new HashSet<>();

        for(DeferredHolder<Block, ? extends Block> block : registry.getEntries()) {
            if (block.get().isEnabled(this.enabledFeatures)) {
                ResourceLocation resourcelocation = block.get().getLootTable();
                if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                    LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                    if (loottable$builder == null) {
                        throw new IllegalStateException(
                                String.format(Locale.ROOT, "Missing loottable '%s' for '%s'", resourcelocation, BuiltInRegistries.BLOCK.getKey(block.get()))
                        );
                    }

                    p_249322_.accept(resourcelocation, loottable$builder);
                }
            }
        }

        if (!this.map.isEmpty()) {
            throw new IllegalStateException("Created block loot tables for non-blocks: " + this.map.keySet());
        }
    }

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
}
