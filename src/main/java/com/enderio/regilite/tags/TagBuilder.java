/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.tags;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class TagBuilder<T> {
    private final TagKey<T> tagKey;
    private final ObjectList<Supplier<? extends T>> entries = new ObjectArrayList<>();

    public TagBuilder(TagKey<T> tagKey) {
        this.tagKey = tagKey;
    }

    public TagKey<T> tagKey() {
        return tagKey;
    }

    @ApiStatus.Internal
    public Stream<Supplier<? extends T>> entries() {
        return entries.stream();
    }

    public TagBuilder<T> add(T entry) {
        return add(() -> entry);
    }

    public TagBuilder<T> add(Supplier<? extends T> entry) {
        this.entries.add(entry);
        return this;
    }

    public TagBuilder<T> addAll(Supplier<? extends T>... entries) {
        this.entries.addAll(Arrays.stream(entries).toList());
        return this;
    }

    // TODO: Support for adding other tags and optional entries.
}
