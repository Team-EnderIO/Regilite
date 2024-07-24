package com.enderio.regilite.data;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApiStatus.Internal
public class BundledDataProvider implements DataProvider {
    private final String modId;
    private final List<DataProvider> subProviders = new ArrayList<>();

    public BundledDataProvider(String modId) {
        this.modId = modId;
    }

    public <T extends DataProvider> T addSubProvider(T provider) {
        subProviders.add(provider);
        return provider;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        for (DataProvider provider : subProviders) {
            list.add(provider.run(cachedOutput));
        }
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Regilite Data (" + modId + ") v2"; // TODO: v2 is temp
    }
}
