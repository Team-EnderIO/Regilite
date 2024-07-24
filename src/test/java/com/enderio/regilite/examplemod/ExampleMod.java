/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.examplemod;

import com.enderio.regilite.Regilite;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod {
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Regilite REGILITE = new Regilite(MODID);

    public ExampleMod(IEventBus modEventBus) {
        Blocks.register(modEventBus);
        Items.register(modEventBus);
        CreativeTabs.register(modEventBus);
        Fluids.register(modEventBus);
        BlockEntities.register(modEventBus);
        Menus.register(modEventBus);
        REGILITE.register(modEventBus);
    }

    public static Regilite getRegilite() {
        return REGILITE;
    }
}
