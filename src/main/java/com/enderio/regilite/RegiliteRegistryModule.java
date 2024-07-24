package com.enderio.regilite;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface RegiliteRegistryModule<R, D extends DeferredRegister<R>> {
    ResourceKey<Registry<R>> registry();

    D deferredRegister();
}
