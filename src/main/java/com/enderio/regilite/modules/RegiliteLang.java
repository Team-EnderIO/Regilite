package com.enderio.regilite.modules;

import com.enderio.regilite.RegiliteDataModule;
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
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class RegiliteLang implements RegiliteDataModule {
    private final String modId;
    private final Object2ObjectMap<Supplier<String>, String> entries = new Object2ObjectOpenHashMap<>();

    public RegiliteLang(String modId) {
        this.modId = modId;
    }

    public MutableComponent addTranslation(String prefix, ResourceLocation location, String translation) {
        return addTranslation(prefix + "." + location.toLanguageKey(), translation);
    }

    public MutableComponent addTranslation(String key, String translation) {
        entries.put(() -> key, translation);
        return Component.translatable(key);
    }

    public void addTranslation(Supplier<String> keySupplier, String translation) {
        entries.put(keySupplier, translation);
    }

    @Override
    public void addDataProviders(GatherDataEvent event, BiConsumer<Boolean, DataProvider> addProvider) {
        addProvider.accept(event.includeClient(), new DefaultProvider(event.getGenerator().getPackOutput(), this.modId, "en_us"));
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
