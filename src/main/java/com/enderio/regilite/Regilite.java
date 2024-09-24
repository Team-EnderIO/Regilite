/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import com.enderio.regilite.blockentities.RegiliteBlockEntities;
import com.enderio.regilite.blocks.RegiliteBlocks;
import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.entities.RegiliteEntities;
import com.enderio.regilite.events.ScreenEvents;
import com.enderio.regilite.fluids.RegiliteFluidTypes;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.loot.RegiliteLootTables;
import com.enderio.regilite.registry.MenuRegistry;
import com.enderio.regilite.tags.RegiliteTags;
import com.enderio.regilite.utils.BundledDataProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Regilite {

    private final String modId;

    private final List<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> menus = new ArrayList<>();

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;

    private final RegiliteLootTables lootTablesModule;

    private final RegiliteItems itemsModule;
    private final RegiliteBlocks blocksRegistry;
    private final RegiliteBlockEntities blockEntityRegistry;
    private final RegiliteFluidTypes fluidTypesModule;
    private final RegiliteEntities entitiesModule;

    private final RegiliteDataProvider dataProvider;

    private final ObjectList<RegiliteModuleDataGen> modulesWithDataGeneration = new ObjectArrayList<>();
    private final ObjectList<RegiliteModuleEvents> modulesWithEvents = new ObjectArrayList<>();

    private final DeferredRegister.DataComponents dataComponentsRegistry;

    public Regilite(String modId) {
        this.modId = modId;
        this.dataProvider = new RegiliteDataProvider(this);

        this.langModule = registerModule(new RegiliteLang(modId));
        this.tagsModule = registerModule(new RegiliteTags(modId));
        this.lootTablesModule = registerModule(new RegiliteLootTables());

        this.itemsModule = registerModule(RegiliteItems.create(this));
        this.blocksRegistry = registerModule(RegiliteBlocks.create(this));
        this.blockEntityRegistry = registerModule(RegiliteBlockEntities.create(this));
        this.fluidTypesModule = registerModule(RegiliteFluidTypes.create(this));
        this.entitiesModule = registerModule(RegiliteEntities.create(this));

        dataComponentsRegistry = DeferredRegister.createDataComponents(modId);
    }

    private <T> T registerModule(T module) {
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

    public RegiliteLang lang() {
        return langModule;
    }

    public RegiliteTags tags() {
        return tagsModule;
    }

    public RegiliteItems items() {
        return itemsModule;
    }

    public RegiliteBlocks blocks() {
        return blocksRegistry;
    }

    public RegiliteBlockEntities blockEntities() {
        return blockEntityRegistry;
    }

    public RegiliteFluidTypes fluidTypes() {
        return fluidTypesModule;
    }

    public RegiliteEntities entities() {
        return entitiesModule;
    }

    public DeferredRegister.DataComponents dataComponents() {
        return dataComponentsRegistry;
    }

    public RegiliteLootTables lootTables() {
        return lootTablesModule;
    }

    public void register(IEventBus modbus) {
        dataProvider.register(modbus);
        dataComponentsRegistry.register(modbus);

        modbus.addListener(this::onGatherData);

        for (var module : modulesWithEvents) {
            module.register(modbus);
        }

        if (FMLEnvironment.dist.isClient()) {
            modbus.addListener(new ScreenEvents(this)::screenEvent);
        }
    }

    private void onGatherData(GatherDataEvent event) {
        var provider = new BundledDataProvider(modId);

        for (var module : modulesWithDataGeneration) {
            module.gatherProviders(event, provider::addSubProvider);
        }

        event.getGenerator().addProvider(true, provider);
    }

    public MenuRegistry menuRegistry() {
        return MenuRegistry.create(this);
    }

    public List<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> getMenus() {
        return menus;
    }

    public void addMenus(Collection<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> entries) {
        this.menus.addAll(entries);
    }
}
