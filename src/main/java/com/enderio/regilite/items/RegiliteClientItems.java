/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.items;

import com.enderio.regilite.blocks.RegiliteBlocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class RegiliteClientItems {
    private final RegiliteItems regiliteItems;

    public RegiliteClientItems(RegiliteItems regiliteItems) {
        this.regiliteItems = regiliteItems;
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registerItemColor(RegisterColorHandlersEvent.Item event) {
        regiliteItems.itemBuilders().forEach(itemBuilder -> {
            if (itemBuilder.colorSupplier != null) {
                event.register(itemBuilder.colorSupplier.get().get(), itemBuilder.get());
            }
        });
    }
}
