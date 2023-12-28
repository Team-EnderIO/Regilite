package com.example.examplemod.data;

import com.example.examplemod.registry.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EnderLangProvider extends LanguageProvider {
    private final List<DeferredHolder<?, ?>> entries = new ArrayList<>();

    public EnderLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    public void add(List<DeferredHolder<?, ?>> entries) {
        this.entries.addAll(entries);
    }

    @Override
    protected void addTranslations() {
        for (DeferredHolder<?, ?> entry : entries) {
            Pair<String, String> translation = ((ITranslatable) entry).getTranslation();
            if (!translation.getB().isEmpty())
                this.add(translation.getA(), translation.getB());
        }
    }
}
