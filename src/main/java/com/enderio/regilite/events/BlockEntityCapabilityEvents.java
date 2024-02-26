package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.registry.BlockEntityRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BlockEntityCapabilityEvents {

    public BlockEntityCapabilityEvents() {

    }

    @SuppressWarnings("rawtypes")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var beHolder : BlockEntityRegistry.getRegistered()) {
            if (beHolder instanceof RegiliteBlockEntity regiliteBlockEntity) {
                regiliteBlockEntity.registerCapabilityProviders(event);
            }
        }
    }
}
