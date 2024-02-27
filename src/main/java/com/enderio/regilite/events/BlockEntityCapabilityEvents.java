package com.enderio.regilite.events;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteBlockEntity;
import com.enderio.regilite.registry.BlockEntityRegistry;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class BlockEntityCapabilityEvents {

    private final Regilite regilite;

    public BlockEntityCapabilityEvents(Regilite regilite) {
        this.regilite = regilite;
    }

    @SuppressWarnings("rawtypes")
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var beHolder : regilite.getBlockEntities()) {
            if (beHolder instanceof RegiliteBlockEntity regiliteBlockEntity) {
                regiliteBlockEntity.registerCapabilityProviders(event);
            }
        }
    }
}
