package com.example.examplemod.data;

import com.example.examplemod.ExampleMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.function.TriFunction;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExampleMod.MODID)
public class EnderDataProvider implements DataProvider {
    private final String modid;
    private final List<DeferredHolder<?, ?>> entries = new ArrayList<>();
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

    public <T> void addTranslations(Collection<DeferredHolder<T, ? extends T>> entries) {
        this.entries.addAll(entries);
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
            enUs.add(provider.entries);
            provider.subProviders.add(enUs);
            event.getGenerator().addProvider(true, provider);
        }

    }
}
