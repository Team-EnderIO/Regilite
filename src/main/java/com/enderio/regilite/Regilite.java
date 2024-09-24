/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import com.enderio.regilite.blockentities.RegiliteBlockEntities;
import com.enderio.regilite.blocks.RegiliteBlocks;
import com.enderio.regilite.entities.RegiliteEntities;
import com.enderio.regilite.fluids.RegiliteFluidTypes;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.loot.RegiliteLootTables;
import com.enderio.regilite.menus.RegiliteMenus;
import com.enderio.regilite.tags.RegiliteTags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Regilite extends AbstractRegilite {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;

    private final RegiliteLootTables lootTablesModule;

    private final RegiliteItems itemsModule;
    private final RegiliteBlocks blocksRegistry;
    private final RegiliteBlockEntities blockEntityRegistry;
    private final RegiliteFluidTypes fluidTypesModule;
    private final RegiliteEntities entitiesModule;
    private final RegiliteMenus regiliteMenus;

    private final DeferredRegister.DataComponents dataComponentsRegistry;

    public Regilite(String modId) {
        super(modId);

        this.langModule = registerModule(new RegiliteLang(modId));
        this.tagsModule = registerModule(new RegiliteTags(modId));
        this.lootTablesModule = registerModule(new RegiliteLootTables());

        this.itemsModule = registerModule(RegiliteItems.create(this));
        this.blocksRegistry = registerModule(RegiliteBlocks.create(this));
        this.blockEntityRegistry = registerModule(RegiliteBlockEntities.create(this));
        this.fluidTypesModule = registerModule(RegiliteFluidTypes.create(this));
        this.entitiesModule = registerModule(RegiliteEntities.create(this));
        this.regiliteMenus = registerModule(RegiliteMenus.create(this));

        dataComponentsRegistry = DeferredRegister.createDataComponents(modId);
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

    public RegiliteMenus menus() {
        return regiliteMenus;
    }

    public DeferredRegister.DataComponents dataComponents() {
        return dataComponentsRegistry;
    }

    public RegiliteLootTables lootTables() {
        return lootTablesModule;
    }

    @Override
    public void register(IEventBus modbus) {
        super.register(modbus);
        dataComponentsRegistry.register(modbus);
    }
}
