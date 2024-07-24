/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import net.neoforged.bus.api.IEventBus;

public interface RegiliteModuleEvents {
    void register(IEventBus modEventBus);
}
