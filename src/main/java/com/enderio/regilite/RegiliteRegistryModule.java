/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface RegiliteRegistryModule<R, D extends DeferredRegister<R>> {
    ResourceKey<Registry<R>> registry();

    D deferredRegister();
}
