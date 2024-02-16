package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.ItemRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ItemCapabilityEvents {
    private final ItemRegistry registry;

    public ItemCapabilityEvents(ItemRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("rawtypes")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var beHolder : registry.getEntries()) {
            if (beHolder instanceof RegiliteItem regiliteItem) {
                regiliteItem.registerCapabilityProviders(event);
            }
        }
    }
}
