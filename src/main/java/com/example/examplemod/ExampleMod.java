package com.example.examplemod;

import com.example.examplemod.data.EnderBlockLootProvider;
import com.example.examplemod.data.EnderItemModelProvider;
import com.example.examplemod.registry.EnderBlockRegistry;
import com.example.examplemod.registry.EnderDeferredBlock;
import com.example.examplemod.registry.EnderDeferredItem;
import com.example.examplemod.registry.EnderItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final EnderBlockRegistry BLOCKS = EnderBlockRegistry.createRegistry(MODID);
    public static final EnderItemRegistry ITEMS = EnderItemRegistry.createRegistry(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final EnderDeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS
            .registerBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE))
            .addBlockTags(BlockTags.MUSHROOM_GROW_BLOCK, BlockTags.LOGS)
            .setTranslation("Test Example Block")
            .setBlockStateProvider(BlockStateProvider::simpleBlock)
            .setLootTable(EnderBlockLootProvider::dropSelf)
            .createBlockItem()
            .addBlockItemTags(ItemTags.PLANKS)
            .setModelProvider(EnderItemModelProvider::basicItem)
            .setTab(CreativeModeTabs.BUILDING_BLOCKS)
            .finishBlockItem();

    public static final EnderDeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build()))
            .addItemTags(ItemTags.WOOL)
            .setTranslation("Test Example Item")
            .setTab(null);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public ExampleMod(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
