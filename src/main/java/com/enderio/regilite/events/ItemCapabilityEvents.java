package com.enderio.regilite.events;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.ItemRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ItemCapabilityEvents {

    private final Regilite regilite;

    public ItemCapabilityEvents(Regilite regilite) {
        this.regilite = regilite;
    }

    @SuppressWarnings("rawtypes")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var beHolder : regilite.getItems()) {
            if (beHolder instanceof RegiliteItem regiliteItem) {
                regiliteItem.registerCapabilityProviders(event);
            }
        }
    }
}
