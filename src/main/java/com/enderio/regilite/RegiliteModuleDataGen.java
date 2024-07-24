package com.enderio.regilite;

import net.minecraft.data.DataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.Consumer;

public interface RegiliteModuleDataGen {
    void gatherProviders(GatherDataEvent event, Consumer<DataProvider> addProvider);
}
