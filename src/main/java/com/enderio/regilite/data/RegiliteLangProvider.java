/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RegiliteLangProvider extends LanguageProvider {
    private final Map<Supplier<String>, String> langEntries = new HashMap<>();

    public RegiliteLangProvider(PackOutput output, String modId, String locale) {
        super(output, modId, locale);
    }

    public void add(Map<Supplier<String>, String> entries) {
        this.langEntries.putAll(entries);
    }

    @Override
    protected void addTranslations() {
        for (Map.Entry<Supplier<String>, String> entry : langEntries.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                this.add(entry.getKey().get(), entry.getValue());
            }
        }
    }
}
