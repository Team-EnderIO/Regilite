package com.example.regilite.data;

import com.example.regilite.Regilite;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Regilite.MODID)
public class EnderDataProvider implements DataProvider {
    private final String modid;
    private final Map<Supplier<String>, String> langEntries = new HashMap<>();
    private final List<DataProvider> subProviders = new ArrayList<>();
    private final List<TriFunction<PackOutput, ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, DataProvider>> serverSubProviderConsumers = new ArrayList<>();
    private static final Map<String, EnderDataProvider> INSTANCES = new HashMap<>();

    protected EnderDataProvider(String modid) {
        this.modid = modid;
    }

    public static EnderDataProvider getInstance(String modid) {
        return INSTANCES.computeIfAbsent(modid, EnderDataProvider::new);
    }

    public void addSubProvider(boolean include, DataProvider provider) {
        if (include) {
            subProviders.add(provider);
        }
    }

    public <T> void addTranslations(Map<Supplier<String>, String> entries) {
        this.langEntries.putAll(entries);
    }

    public MutableComponent addTranslation(String key, String translation) {
        this.langEntries.put(() -> key, translation);
        return Component.translatable(key);
    }

    public void addTranslation(Supplier<String> key, String translation) {
        this.langEntries.put(key, translation);
    }

    public static MutableComponent addTranslation(String prefix, ResourceLocation location, String translation) {
        return getInstance(location.getNamespace()).addTranslation(prefix + "." + location.toLanguageKey(), translation);
    }

    public void addServerSubProvider(TriFunction<PackOutput, ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, DataProvider> function) {
        serverSubProviderConsumers.add(function);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        for (DataProvider provider : subProviders) {
            list.add(provider.run(pOutput));
        }
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Ender IO Data (" + modid + ")";
    }

    @SubscribeEvent
    static void onGatherData(GatherDataEvent event) {
        for (EnderDataProvider provider : INSTANCES.values()) {
            if (event.includeServer()) {
                for (TriFunction<PackOutput, ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, DataProvider> function : provider.serverSubProviderConsumers) {
                    provider.subProviders.add(function.apply(event.getGenerator().getPackOutput(), event.getExistingFileHelper(), event.getLookupProvider()));
                }
            }
            EnderLangProvider enUs = new EnderLangProvider(event.getGenerator().getPackOutput(), provider.modid, "en_us");
            enUs.add(provider.langEntries);
            provider.subProviders.add(enUs);
            event.getGenerator().addProvider(true, provider);
        }

    }
}
