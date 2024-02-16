package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.registry.BlockEntityRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BlockEntityCapabilityEvents {
    private final BlockEntityRegistry registry;

    public BlockEntityCapabilityEvents(BlockEntityRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("rawtypes")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var beHolder : registry.getEntries()) {
            if (beHolder instanceof RegiliteBlockEntity regiliteBlockEntity) {
                regiliteBlockEntity.registerCapabilityProviders(event);
            }
        }
    }
}
