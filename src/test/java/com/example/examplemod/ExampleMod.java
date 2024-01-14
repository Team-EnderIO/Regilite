package com.example.examplemod;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
        BlockEntities.register(modEventBus);
        Menus.register(modEventBus);
    }
}
