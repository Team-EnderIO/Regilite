package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.ItemRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ItemCapabilityEvents {

    public ItemCapabilityEvents() {

    }

    @SuppressWarnings("rawtypes")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var beHolder : ItemRegistry.getRegistered()) {
            if (beHolder instanceof RegiliteItem regiliteItem) {
                regiliteItem.registerCapabilityProviders(event);
            }
        }
    }
}
