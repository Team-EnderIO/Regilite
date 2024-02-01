package com.example.regilite.registry;

import com.example.regilite.holder.RegiliteType;
import net.minecraft.tags.TagKey;

import java.util.Set;

public interface ITaggable<R extends RegiliteType<T>, T> {

    Set<TagKey<T>> getTags();
    R addTags(TagKey<T>... tags);
}
