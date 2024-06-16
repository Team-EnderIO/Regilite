package com.enderio.regilite.data;

import com.enderio.regilite.Regilite;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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
    private final Regilite regilite;

    public RegiliteDataProvider(Regilite regilite) {
        this.modid = regilite.getModid();
        this.regilite = regilite;
    }

    public void register(IEventBus modbus) {
        modbus.addListener(this::onGatherData);
    }

    public MutableComponent addTranslation(String key, String translation) {
        this.langEntries.put(() -> key, translation);
        return Component.translatable(key);
    }

    public void addTranslation(Supplier<String> key, String translation) {
        this.langEntries.put(key, translation);
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
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        RegiliteLangProvider enUs = new RegiliteLangProvider(packOutput, this.modid, "en_us");
        enUs.add(this.langEntries);
        this.subProviders.add(enUs);

        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.BLOCK, b -> b.builtInRegistryHolder().key(), registries, modid, existingFileHelper, regilite.getBlock()));
        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.ITEM, b -> b.builtInRegistryHolder().key(), registries, modid, existingFileHelper, regilite.getItems()));
        this.subProviders.add(new RegiliteTagProvider.FluidTagProvider(packOutput, Registries.FLUID, b -> b.builtInRegistryHolder().key(), registries, modid, existingFileHelper, regilite.getFluids()));
        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.BLOCK_ENTITY_TYPE, b -> b.builtInRegistryHolder().key(), registries, modid, existingFileHelper, regilite.getBlockEntities()));
        this.subProviders.add(new RegiliteTagProvider<>(packOutput, Registries.ENTITY_TYPE, b -> b.builtInRegistryHolder().key(), registries, modid, existingFileHelper, regilite.getEntities()));

        this.subProviders.add(new RegiliteBlockStateProvider(packOutput, modid, existingFileHelper, regilite.getBlock()));
        this.subProviders.add(new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry((provider) -> new RegiliteBlockLootProvider(Set.of(), regilite.getBlock(), provider), LootContextParamSets.BLOCK)), registries));

        this.subProviders.add(new RegiliteItemModelProvider(packOutput, modid, existingFileHelper, regilite.getItems()));
        event.getGenerator().addProvider(true, this);
    }
}
