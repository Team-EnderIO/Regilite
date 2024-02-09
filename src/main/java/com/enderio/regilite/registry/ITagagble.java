package com.enderio.regilite.registry;

import net.minecraft.tags.TagKey;

import java.util.Set;

public interface ITagagble<T> {

    Set<TagKey<T>> getTags();
}
