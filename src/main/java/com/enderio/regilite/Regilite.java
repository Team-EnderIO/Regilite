package com.enderio.regilite;

import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.events.BlockEntityCapabilityEvents;
import com.enderio.regilite.events.BlockEntityRendererEvents;
import com.enderio.regilite.events.ColorEvents;
import com.enderio.regilite.events.EntityRendererEvents;
import com.enderio.regilite.events.FluidRenderTypeEvents;
import com.enderio.regilite.events.ItemCapabilityEvents;
import com.enderio.regilite.events.ScreenEvents;
import com.enderio.regilite.registry.ItemRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;

public class Regilite {

    private final String modid;

    public Regilite(String modid) {
        this.modid = modid;
    }

    public void register(IEventBus modbus) {
        RegiliteDataProvider.register(modid, modbus);

        if (FMLEnvironment.dist.isClient()) {
            modbus.addListener(new ColorEvents.Blocks()::registerBlockColor);

            modbus.addListener(new ColorEvents.Items()::registerItemColor);
            modbus.addListener(new ItemCapabilityEvents()::registerCapabilities);
            modbus.addListener(ItemRegistry::addCreative);

            modbus.addListener(new BlockEntityRendererEvents()::registerBER);
            modbus.addListener(new BlockEntityCapabilityEvents()::registerCapabilities);

            modbus.addListener(new FluidRenderTypeEvents()::registerRenderTypes);

            modbus.addListener(new EntityRendererEvents()::registerER);

            modbus.addListener(new ScreenEvents()::screenEvent);
        }
    }
}
