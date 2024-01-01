package com.example.examplemod.testmod;

import com.example.examplemod.data.EnderBlockLootProvider;
import com.example.examplemod.data.EnderItemModelProvider;
import com.example.examplemod.registry.EnderBlockRegistry;
import com.example.examplemod.registry.EnderDeferredBlock;
import com.example.examplemod.registry.EnderDeferredItem;
import com.example.examplemod.registry.EnderItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ExampleMod(IEventBus modEventBus) {
        Blocks.register(modEventBus);
        Items.register(modEventBus);
        CreativeTabs.register(modEventBus);
        Fluids.register(modEventBus);
    }
}
