/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import com.enderio.regilite.blockentities.RegiliteBlockEntityTypes;
import com.enderio.regilite.blocks.RegiliteBlocks;
import com.enderio.regilite.entities.RegiliteEntityTypes;
import com.enderio.regilite.fluids.RegiliteFluidTypes;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.loot.RegiliteLootTables;
import com.enderio.regilite.menus.RegiliteMenuTypes;
import com.enderio.regilite.tags.RegiliteTags;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Regilite extends AbstractRegilite {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;

    private final RegiliteLootTables lootTablesModule;

    private final RegiliteItems items;
    private final RegiliteBlocks blocks;
    private final RegiliteBlockEntityTypes blockEntityTypes;
    private final RegiliteFluidTypes fluidTypes;
    private final RegiliteEntityTypes entityTypes;
    private final RegiliteMenuTypes menuTypes;

    private final DeferredRegister.DataComponents dataComponentsRegistry;

    public Regilite(String modId) {
        super(modId);

        this.langModule = registerModule(new RegiliteLang(modId));
        this.tagsModule = registerModule(new RegiliteTags(modId));
        this.lootTablesModule = registerModule(new RegiliteLootTables());

        this.items = registerModule(RegiliteItems.create(this));
        this.blocks = registerModule(RegiliteBlocks.create(this));
        this.blockEntityTypes = registerModule(RegiliteBlockEntityTypes.create(this));
        this.fluidTypes = registerModule(RegiliteFluidTypes.create(this));
        this.entityTypes = registerModule(RegiliteEntityTypes.create(this));
        this.menuTypes = registerModule(RegiliteMenuTypes.create(this));

        dataComponentsRegistry = DeferredRegister.createDataComponents(modId);
    }

    public RegiliteLang lang() {
        return langModule;
    }

    public RegiliteTags tags() {
        return tagsModule;
    }

    public RegiliteItems items() {
        return items;
    }

    public RegiliteBlocks blocks() {
        return blocks;
    }

    public RegiliteBlockEntityTypes blockEntityTypes() {
        return blockEntityTypes;
    }

    public RegiliteFluidTypes fluidTypes() {
        return fluidTypes;
    }

    public RegiliteEntityTypes entityTypes() {
        return entityTypes;
    }

    public RegiliteMenuTypes menuTypes() {
        return menuTypes;
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
