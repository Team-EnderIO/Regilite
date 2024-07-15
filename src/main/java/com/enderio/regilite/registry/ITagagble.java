/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.registry;

import com.enderio.regilite.holder.RegiliteEntity;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Set;

public interface ITagagble<T> {
    Set<TagKey<T>> getTags();
}
