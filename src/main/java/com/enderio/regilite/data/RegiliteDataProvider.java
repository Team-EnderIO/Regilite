package com.enderio.regilite.data;

import com.enderio.regilite.registry.BlockEntityRegistry;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.registry.EntityRegistry;
import com.enderio.regilite.registry.FluidRegister;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RegiliteDataProvider implements DataProvider {
    private final String modid;
    private final Map<Supplier<String>, String> langEntries = new HashMap<>();
    private final List<DataProvider> subProviders = new ArrayList<>();
    private final List<TriFunction<PackOutput, ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, DataProvider>> serverSubProviderConsumers = new ArrayList<>();
    private static final Map<String, RegiliteDataProvider> INSTANCES = new HashMap<>();
    private boolean registered = false;

    protected RegiliteDataProvider(String modid) {
        this.modid = modid;
    }

    public static RegiliteDataProvider getInstance(String modid) {
        return INSTANCES.computeIfAbsent(modid, RegiliteDataProvider::new);
    }

    public static RegiliteDataProvider register(String modid, IEventBus modbus) {
        RegiliteDataProvider provider = INSTANCES.computeIfAbsent(modid, RegiliteDataProvider::new);
        if (!provider.registered) {
            modbus.addListener(provider::onGatherData);
            provider.registered = true;
        }
        return provider;
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

    void onGatherData(GatherDataEvent event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        if (event.includeServer()) {
            for (TriFunction<PackOutput, ExistingFileHelper, CompletableFuture<HolderLookup.Provider>, DataProvider> function : this.serverSubProviderConsumers) {
                this.subProviders.add(function.apply(packOutput, existingFileHelper, lookupProvider));
            }
        }
        RegiliteLangProvider enUs = new RegiliteLangProvider(packOutput, this.modid, "en_us");
        enUs.add(this.langEntries);
        this.subProviders.add(enUs);

        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.BLOCK, b -> b.builtInRegistryHolder().key(), lookupProvider, modid, existingFileHelper, BlockRegistry.getRegistered()));
        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.ITEM, b -> b.builtInRegistryHolder().key(), lookupProvider, modid, existingFileHelper, ItemRegistry.getRegistered()));
        this.subProviders.add(new RegiliteTagProvider.FluidTagProvider(packOutput, Registries.FLUID, b -> b.builtInRegistryHolder().key(), lookupProvider, modid, existingFileHelper, FluidRegister.getRegistered()));
        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.BLOCK_ENTITY_TYPE, b -> b.builtInRegistryHolder().key(), lookupProvider, modid, existingFileHelper, BlockEntityRegistry.getRegistered()));
        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.ENTITY_TYPE, b -> b.builtInRegistryHolder().key(), lookupProvider, modid, existingFileHelper, EntityRegistry.getRegistered()));

        this.subProviders.add(new RegiliteBlockStateProvider(packOutput, modid, existingFileHelper, BlockRegistry.getRegistered()));
        this.subProviders.add(new LootTableProvider(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(() -> new RegiliteBlockLootProvider(Set.of(), BlockRegistry.getRegistered()), LootContextParamSets.BLOCK))));

        this.subProviders.add(new RegiliteItemModelProvider(packOutput, modid, existingFileHelper, ItemRegistry.getRegistered()));
        event.getGenerator().addProvider(true, this);
    }
}
