/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.registry;

import net.minecraft.tags.TagKey;

import java.util.Set;

public interface ITagagble<T> {
    Set<TagKey<T>> getTags();
}
