package com.enderio.regilite.blocks;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RegiliteClientBlocks {
    private final RegiliteBlocks regiliteBlocks;

    public RegiliteClientBlocks(RegiliteBlocks regiliteBlocks) {
        this.regiliteBlocks = regiliteBlocks;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registerBlockColor(RegisterColorHandlersEvent.Block event) {
        regiliteBlocks.blockBuilders().forEach(blockBuilder -> {
            if (blockBuilder.blockColor() != null) {
                event.register(blockBuilder.blockColor().get().get(), blockBuilder.get());
            }
        });
    }
}
