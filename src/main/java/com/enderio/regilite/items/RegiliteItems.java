/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.items;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.RegiliteModuleEvents;
import com.enderio.regilite.RegiliteRegistryModule;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteItems implements RegiliteRegistryModule<Item, DeferredRegister.Items>, RegiliteModuleEvents {
    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final DeferredRegister.Items deferredRegister;

    // Tracks all of the builders so that they can be used for data-generation.
    private final ObjectList<ItemBuilder<? extends Item>> items = new ObjectArrayList<>();

    private RegiliteItems(RegiliteLang langModule, RegiliteTags tagsModule, DeferredRegister.Items deferredRegister) {
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.deferredRegister = deferredRegister;
    }

    @ApiStatus.Internal
    public static RegiliteItems create(Regilite regilite) {
        return new RegiliteItems(regilite.lang(), regilite.tags(), DeferredRegister.createItems(regilite.getModId()));
    }

    public <T extends Item> ItemBuilder<T> create(String name, Function<Item.Properties, ? extends T> func, Item.Properties props) {
        return create(name, () -> func.apply(props));
    }

    public <T extends Item> ItemBuilder<T> create(String name, Supplier<? extends T> supplier) {
        DeferredItem<T> holder = deferredRegister.register(name, supplier);
        var builder = new ItemBuilder<>(holder, langModule, tagsModule);
        items.add(builder);
        return builder;
    }

    public ItemBuilder<Item> createSimple(String name) {
        return createSimple(name, new Item.Properties());
    }

    public ItemBuilder<Item> createSimple(String name, Item.Properties properties) {
        DeferredItem<Item> holder = deferredRegister.registerSimpleItem(name, properties);
        var builder = new ItemBuilder<>(holder, langModule, tagsModule);
        items.add(builder);
        return builder;
    }

    public ItemBuilder<BlockItem> createSimpleBlockItem(Holder<Block> block) {
        return createSimpleBlockItem(block, new Item.Properties());
    }

    public ItemBuilder<BlockItem> createSimpleBlockItem(Holder<Block> block, Item.Properties properties) {
        String name = (block.unwrapKey().orElseThrow()).location().getPath();
        return createSimpleBlockItem(name, block::value, properties);
    }

    public ItemBuilder<BlockItem> createSimpleBlockItem(String name, Supplier<? extends Block> block) {
        return createSimpleBlockItem(name, block, new Item.Properties());
    }

    public ItemBuilder<BlockItem> createSimpleBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
        DeferredItem<BlockItem> holder = deferredRegister.registerSimpleBlockItem(name, block, properties);
        var builder = new ItemBuilder<>(holder, langModule, tagsModule);
        items.add(builder);
        return builder;
    }

    @Override
    public ResourceKey<Registry<Item>> registry() {
        return Registries.ITEM;
    }

    @Override
    public DeferredRegister.Items deferredRegister() {
        return deferredRegister;
    }

    @Override
    public void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);
    }
}
