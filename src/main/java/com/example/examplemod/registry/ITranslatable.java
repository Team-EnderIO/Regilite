package com.example.examplemod.registry;

import net.minecraft.resources.ResourceLocation;
import oshi.util.tuples.Pair;

public interface ITranslatable {
    Pair<String, String> getTranslation();
}
