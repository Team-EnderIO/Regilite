package com.enderio.regilite;

import com.enderio.regilite.utils.BundledDataProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Abstract Regilite implementation that handles registration of module events.
 * This can be used if a mod wants to implement its own modules to replace or extend existing ones.
 * This might incur some more boilerplate, but is more ergonomic than shadowing the existing Regilite methods.
 * If you only want to add a custom registry to Regilite, extend Regilite instead.
 */
public abstract class AbstractRegilite {
    private final String modId;

    private final ObjectList<RegiliteModuleDataGen> modulesWithDataGeneration = new ObjectArrayList<>();
    private final ObjectList<RegiliteModuleEvents> modulesWithEvents = new ObjectArrayList<>();

    protected AbstractRegilite(String modId) {
        this.modId = modId;
    }

    /**
     * Register a module within the Regiltie container.
     * This will hook the different module types into the correct places (events and datagen for example).
     */
    protected <T> T registerModule(T module) {
        if (module instanceof RegiliteModuleDataGen dataGenModule) {
            modulesWithDataGeneration.add(dataGenModule);
        }

        if (module instanceof RegiliteModuleEvents eventsModule) {
            modulesWithEvents.add(eventsModule);
        }

        return module;
    }

    public String modId() {
        return modId;
    }

    /**
     * Register all event handlers for Regilite's modules.
     * You should perform this in your mod entrypoint.
     * @param modEventBus The mod event bus.
     */
    public void register(IEventBus modEventBus) {
        modEventBus.addListener(this::onGatherData);

        for (var module : modulesWithEvents) {
            module.register(modEventBus);
        }
    }

    protected void onGatherData(GatherDataEvent event) {
        var provider = new BundledDataProvider(modId);

        for (var module : modulesWithDataGeneration) {
            module.gatherProviders(event, provider::addSubProvider);
        }

        event.getGenerator().addProvider(true, provider);
    }
}
