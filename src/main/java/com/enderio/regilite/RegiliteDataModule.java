package com.enderio.regilite;

import net.minecraft.data.DataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface RegiliteDataModule {
    void addDataProviders(GatherDataEvent event, BiConsumer<Boolean, DataProvider> addProvider);
}
