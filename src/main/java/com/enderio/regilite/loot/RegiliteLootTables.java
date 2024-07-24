package com.enderio.regilite.loot;

import com.enderio.regilite.RegiliteModuleDataGen;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

// This module is slightly less expressive than I would have liked, but at least it is a start.
// Really it should only be used internally, as there are better ways of doing loot tables outside of Regilite.
// I don't think rich loot table support is something we need to provide, given I think its expressive enough in vanilla.
public class RegiliteLootTables implements RegiliteModuleDataGen {

    private ObjectList<Supplier<LootTableProvider.SubProviderEntry>> providerFactories = new ObjectArrayList<>();

    @ApiStatus.Internal
    public RegiliteLootTables() {
    }

    public void addLootTableProvider(Supplier<LootTableProvider.SubProviderEntry> providerFactory) {
        providerFactories.add(providerFactory);
    }

    @Override
    public void gatherProviders(GatherDataEvent event, Consumer<DataProvider> addProvider) {
        if (!event.includeServer()) {
            return;
        }

        var packOutput = event.getGenerator().getPackOutput();
        var lookupProvider = event.getLookupProvider();

        addProvider.accept(new LootTableProvider(packOutput, Collections.emptySet(),
                providerFactories.stream().map(Supplier::get).toList(), lookupProvider));
    }
}
