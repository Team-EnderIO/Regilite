package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleBlock;
import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import com.enderio.regilite.data.RegiliteBlockLootProvider;
import com.enderio.regilite.data.RegiliteItemModelProvider;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;

public class Blocks {
    public static final BlockRegistry BLOCKS = BlockRegistry.createRegistry(ExampleMod.MODID);
    public static final ItemRegistry ITEMS = ItemRegistry.createRegistry(ExampleMod.MODID);

    public static final RegiliteBlock<ExampleBlock> EXAMPLE_BLOCK = BLOCKS
            .registerBlock("example_block", ExampleBlock::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .addBlockTags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .setTranslation("Test Example Block")
            .setColorSupplier(() -> ExampleColors.BLOCK)
            .setBlockStateProvider((prov, ctx) -> prov.simpleBlock(ctx.get()))
            .setLootTable(RegiliteBlockLootProvider::dropSelf)
            .createBlockItem(ITEMS, item -> item
                    .addItemTags(ItemTags.PLANKS)
                    .setModelProvider((prov, ctx) -> prov.basicItem(ctx.get()))
                    .setTab(CreativeModeTabs.BUILDING_BLOCKS)
                    .setTab(CreativeTabs.EXAMPLE_TAB.getKey()));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
