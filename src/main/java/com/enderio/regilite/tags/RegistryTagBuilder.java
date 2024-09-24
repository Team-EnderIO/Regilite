/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.tags;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistryTagBuilder<T> {
    private final ResourceKey<Registry<T>> registryKey;
    final Function<T, ResourceKey<T>> keyExtractor;
    final Object2ObjectMap<TagKey<T>, TagBuilder<T>> tags = new Object2ObjectOpenHashMap<>();

    public RegistryTagBuilder(ResourceKey<Registry<T>> registryKey, Function<T, ResourceKey<T>> keyExtractor) {
        this.registryKey = registryKey;
        this.keyExtractor = keyExtractor;
    }

    public ResourceKey<Registry<T>> registry() {
        return registryKey;
    }

    public TagBuilder<T> tag(TagKey<T> tagKey) {
        return tags.computeIfAbsent(tagKey, (TagKey<T> t) -> new TagBuilder<T>(t));
    }

    @SafeVarargs
    public final void addToTags(Supplier<? extends T> entry, TagKey<T>... tags) {
        for (TagKey<T> tag : tags) {
            tag(tag).add(entry);
        }
    }
}
