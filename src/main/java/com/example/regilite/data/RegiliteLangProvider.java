package com.example.regilite.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RegiliteLangProvider extends LanguageProvider {
    private final Map<Supplier<String>, String> langEntries = new HashMap<>();

    public RegiliteLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    public void add(Map<Supplier<String>, String> entries) {
        this.langEntries.putAll(entries);
    }

    @Override
    protected void addTranslations() {
        for (Map.Entry<Supplier<String>, String> entry : langEntries.entrySet()) {
            if (!entry.getValue().isEmpty())
                this.add(entry.getKey().get(), entry.getValue());
        }
    }
}
