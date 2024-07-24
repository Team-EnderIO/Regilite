/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import net.minecraft.data.DataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.Consumer;

public interface RegiliteModuleDataGen {
    void gatherProviders(GatherDataEvent event, Consumer<DataProvider> addProvider);
}
