package com.example.examplemod;

import com.example.examplemod.exampleclasses.ExampleBlock;
import com.example.regilite.data.EnderBlockLootProvider;
import com.example.regilite.data.EnderItemModelProvider;
import com.example.regilite.registry.EnderBlockRegistry;
import com.example.regilite.registry.EnderDeferredBlock;
import com.example.regilite.registry.EnderItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

public class Blocks {
    public static final EnderBlockRegistry BLOCKS = EnderBlockRegistry.createRegistry(ExampleMod.MODID);
    public static final EnderItemRegistry ITEMS = EnderItemRegistry.createRegistry(ExampleMod.MODID);

    public static final EnderDeferredBlock<ExampleBlock> EXAMPLE_BLOCK = BLOCKS
            .registerBlock("example_block", ExampleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .addBlockTags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .setTranslation("Test Example Block")
            .setBlockStateProvider(BlockStateProvider::simpleBlock)
            .setLootTable(EnderBlockLootProvider::dropSelf)
            .createBlockItem(ITEMS)
            .addBlockItemTags(ItemTags.PLANKS)
            .setModelProvider(EnderItemModelProvider::basicItem)
            .setTab(CreativeModeTabs.BUILDING_BLOCKS)
            .setTab(CreativeTabs.EXAMPLE_TAB.getKey())
            .finishBlockItem();

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}