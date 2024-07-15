/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.data;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DataGenContext<R, E extends R> implements Supplier<E> {
    private final Supplier<E> entry;
    private final String modid;
    private final String name;

    public DataGenContext(ResourceLocation id, Supplier<E> entry) {
        this.modid = id.getNamespace();
        this.name = id.getPath();
        this.entry = entry;
    }

    @Override
    public E get() {
        return entry.get();
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }
}
