/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite;

import com.enderio.regilite.utils.BundledDataProvider;
import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.events.BlockEntityCapabilityEvents;
import com.enderio.regilite.events.BlockEntityRendererEvents;
import com.enderio.regilite.events.ColorEvents;
import com.enderio.regilite.events.EntityRendererEvents;
import com.enderio.regilite.events.FluidRenderTypeEvents;
import com.enderio.regilite.events.ItemCapabilityEvents;
import com.enderio.regilite.events.ScreenEvents;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.blocks.RegiliteBlocks;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.loot.RegiliteLootTables;
import com.enderio.regilite.tags.RegiliteTags;
import com.enderio.regilite.registry.BlockEntityRegistry;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.registry.EntityRegistry;
import com.enderio.regilite.registry.FluidRegistry;
import com.enderio.regilite.registry.ItemRegistry;
import com.enderio.regilite.registry.MenuRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Regilite {

    private final String modId;

    private final List<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> blockentities = new ArrayList<>();
    private final List<DeferredHolder<Block, ? extends Block>> blocks = new ArrayList<>();
    private final List<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entities = new ArrayList<>();
    private final List<DeferredHolder<FluidType, ? extends FluidType>> fluids = new ArrayList<>();
    private final List<DeferredHolder<Item, ? extends Item>> items = new ArrayList<>();
    private final List<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> menus = new ArrayList<>();

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;

    private final RegiliteLootTables lootTablesModule;

    private final RegiliteItems itemsModule;
    private final RegiliteBlocks blocksRegistry;

    private final RegiliteDataProvider dataProvider;

    private final ObjectList<RegiliteModuleDataGen> modulesWithDataGeneration = new ObjectArrayList<>();
    private final ObjectList<RegiliteModuleEvents> modulesWithEvents = new ObjectArrayList<>();

    //private final DeferredRegister.DataComponents dataComponentsRegistry;

    public Regilite(String modId) {
        this.modId = modId;
        this.dataProvider = new RegiliteDataProvider(this);

        this.langModule = registerModule(new RegiliteLang(modId));
        this.tagsModule = registerModule(new RegiliteTags(modId));
        this.lootTablesModule = registerModule(new RegiliteLootTables());

        this.itemsModule = registerModule(RegiliteItems.create(this));
        this.blocksRegistry = registerModule(RegiliteBlocks.create(this));
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

    public DeferredRegister.DataComponents dataComponents() {
        throw new NotImplementedException();
    }

    public RegiliteLootTables lootTables() {
        return lootTablesModule;
    }

    public void register(IEventBus modbus) {
        dataProvider.register(modbus);

        modbus.addListener(this::onGatherData);

        for (var module : modulesWithEvents) {
            module.register(modbus);
        }

        modbus.addListener(new ItemCapabilityEvents(this)::registerCapabilities);
        modbus.addListener(new BlockEntityCapabilityEvents(this)::registerCapabilities);

        if (FMLEnvironment.dist.isClient()) {
            modbus.addListener(new ColorEvents.Blocks(this)::registerBlockColor);

            modbus.addListener(new ColorEvents.Items(this)::registerItemColor);
            modbus.addListener(this::addCreative);

            modbus.addListener(new BlockEntityRendererEvents(this)::registerBER);

            modbus.addListener(new FluidRenderTypeEvents(this)::registerRenderTypes);

            modbus.addListener(new EntityRendererEvents(this)::registerER);

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

    public String getModId() {
        return modId;
    }

    public BlockRegistry blockRegistry() {
        return BlockRegistry.create(this);
    }

    public BlockEntityRegistry blockEntityRegistry() {
        return BlockEntityRegistry.create(this);
    }

    public EntityRegistry entityRegistry() {
        return EntityRegistry.create(this);
    }

    public FluidRegistry fluidRegistry() {
        return FluidRegistry.create(this);
    }

    public ItemRegistry itemRegistry() {
        return ItemRegistry.create(this);
    }

    public MenuRegistry menuRegistry() {
        return MenuRegistry.create(this);
    }

    public List<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> getBlockEntities() {
        return blockentities;
    }

    public void addBlockEntities(Collection<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> entries) {
        this.blockentities.addAll(entries);
    }

    public List<DeferredHolder<Block, ? extends Block>> getBlock() {
        return blocks;
    }

    public void addBlocks(Collection<DeferredHolder<Block, ? extends Block>> entries) {
        this.blocks.addAll(entries);
    }

    public List<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> getEntities() {
        return entities;
    }

    public void addEntities(Collection<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entries) {
        this.entities.addAll(entries);
    }

    public List<DeferredHolder<FluidType, ? extends FluidType>> getFluids() {
        return fluids;
    }

    public void addFluids(Collection<DeferredHolder<FluidType, ? extends FluidType>> entries) {
        this.fluids.addAll(entries);
    }

    public List<DeferredHolder<Item, ? extends Item>> getItems() {
        return items;
    }

    public void addItems(Collection<DeferredHolder<Item, ? extends Item>> entries) {
        this.items.addAll(entries);
    }

    public List<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> getMenus() {
        return menus;
    }

    public void addMenus(Collection<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> entries) {
        this.menus.addAll(entries);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        for (DeferredHolder<Item, ? extends Item> item : getItems()) {
            if (item instanceof RegiliteItem) {
                Consumer<CreativeModeTab.Output> outputConsumer = ((RegiliteItem<Item>)item).getTab().get(event.getTabKey());
                if (outputConsumer != null) {
                    outputConsumer.accept(event);
                }
            }
        }
    }

    @Deprecated(forRemoval = true, since = "0.1")
    public void addTranslation(Supplier<String> key, String translation) {
        lang().add(key, translation);
    }
}
