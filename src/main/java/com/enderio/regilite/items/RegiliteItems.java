/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.items;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.lang.RegiliteLang;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

public class RegiliteItems {
    private final RegiliteLang langModule;
    private final DeferredRegister.Items deferredRegister;

    private RegiliteItems(RegiliteLang langModule, DeferredRegister.Items deferredRegister) {
        this.langModule = langModule;
        this.deferredRegister = deferredRegister;
    }

    @ApiStatus.Internal
    public static RegiliteItems create(Regilite regilite) {
        return new RegiliteItems(regilite.lang(), DeferredRegister.createItems(regilite.getModId()));
    }
}
