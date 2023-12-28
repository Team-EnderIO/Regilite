package com.example.examplemod.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public interface ITagagble<T> {

    Set<TagKey<T>> getTags();
}
