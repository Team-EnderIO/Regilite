package com.example.examplemod.data;

import com.example.examplemod.ExampleMod;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExampleMod.MODID)
public class EnderDataProvider implements DataProvider {
    private final String modid;
    private final List<DeferredHolder<Block, ? extends Block>> BLOCKS = new ArrayList<>();
    private final List<DeferredHolder<Item, ? extends Item>> ITEMS = new ArrayList<>();
    private final List<DataProvider> subProviders = new ArrayList<>();
    private final List<BiFunction<PackOutput, ExistingFileHelper, DataProvider>> serverSubProviderConsumers = new ArrayList<>();
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

    public void addBlocks(Collection<DeferredHolder<Block, ? extends Block>> blocks) {
        BLOCKS.addAll(blocks);
    }

    public void addItems(Collection<DeferredHolder<Item, ? extends Item>> items) {
        ITEMS.addAll(items);
    }

    public void addServerSubProvider(BiFunction<PackOutput, ExistingFileHelper, DataProvider> function) {
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
                for (BiFunction<PackOutput, ExistingFileHelper, DataProvider> function : provider.serverSubProviderConsumers) {
                    provider.subProviders.add(function.apply(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
                }
            }
            EnderLangProvider enUs = new EnderLangProvider(event.getGenerator().getPackOutput(), provider.modid, "en_us");
            enUs.addBlocks(provider.BLOCKS);
            enUs.addItems(provider.ITEMS);
            provider.subProviders.add(enUs);
            event.getGenerator().addProvider(true, provider);
        }

    }
}
