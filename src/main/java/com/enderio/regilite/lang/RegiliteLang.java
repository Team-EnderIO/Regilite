/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.lang;

import com.enderio.regilite.RegiliteModuleDataGen;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegiliteLang implements RegiliteModuleDataGen {
    private final String modId;
    private final Object2ObjectMap<Supplier<String>, String> entries = new Object2ObjectOpenHashMap<>();

    public RegiliteLang(String modId) {
        this.modId = modId;
    }

    public MutableComponent add(String prefix, ResourceLocation location, String translation) {
        return add(prefix + "." + location.toLanguageKey(), translation);
    }

    public MutableComponent add(String key, String translation) {
        entries.put(() -> key, translation);
        return Component.translatable(key);
    }

    public void add(Supplier<String> keySupplier, String translation) {
        entries.put(keySupplier, translation);
    }

    @Override
    public void gatherProviders(GatherDataEvent event, Consumer<DataProvider> addProvider) {
        if (!event.includeClient()) {
            return;
        }

        addProvider.accept(new DefaultProvider(event.getGenerator().getPackOutput(), this.modId, "en_us"));
    }

    private class DefaultProvider extends LanguageProvider {
        public DefaultProvider(PackOutput output, String modid, String locale) {
            super(output, modid, locale);
        }

        @Override
        protected void addTranslations() {
            for (Map.Entry<Supplier<String>, String> entry : entries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get(), entry.getValue());
                }
            }
        }

        @Override
        public String getName() {
            return "Regilite " + super.getName();
        }
    }
}
