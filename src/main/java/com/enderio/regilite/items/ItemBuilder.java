/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.items;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.data.DataGenContext;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemBuilder<T extends Item> extends RegiliteBuilder<ItemBuilder<T>, Item, T, DeferredItem<T>> {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;

    protected Object2ObjectMap<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> tabs = new Object2ObjectOpenHashMap<>();

    @Nullable
    protected BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider = (prov, ctx) -> prov.basicItem(ctx.get());

    @Nullable
    protected Supplier<Supplier<ItemColor>> colorSupplier;

    protected List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    public ItemBuilder(DeferredItem<T> holder, RegiliteLang langModule, RegiliteTags tagsModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;

        translation(DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public ItemBuilder<T> translation(String englishTranslation) {
        langModule.addItem(holder, englishTranslation);
        return this;
    }

    public ItemBuilder<T> removeTranslation() {
        langModule.removeItem(holder);
        return this;
    }

    public final ItemBuilder<T> tag(TagKey<Item> tag) {
        tagsModule.items().tag(tag).add(this::get);
        return this;
    }

    @SafeVarargs
    public final ItemBuilder<T> tags(TagKey<Item>... tags) {
        tagsModule.items().addToTags(this::get, tags);
        return this;
    }

    public final ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab) {
        this.tabs.put(tab, output -> output.accept(new ItemStack(this.get())));
        return this;
    }

    public final ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        this.tabs.put(tab, output -> output.accept(new ItemStack(this.get()), visibility));
        return this;
    }

    public final ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTab.Output> output) {
        this.tabs.put(tab, output);
        return this;
    }

    public ItemBuilder<T> model(BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }

    public ItemBuilder<T> itemColor(Supplier<Supplier<ItemColor>> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public <TCap, TContext> ItemBuilder<T> capability(ItemCapability<TCap, TContext> capability, ICapabilityProvider<ItemStack, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    void attachCapabilities(RegisterCapabilitiesEvent event) {
        for (var attachedCapability : attachedCapabilityList) {
            attachedCapability.registerProvider(event, get());
        }
    }

    protected record AttachedCapability<T extends Item, TCap, TContext>(
            ItemCapability<TCap, TContext> capability,
            ICapabilityProvider<ItemStack, TContext, TCap> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, Item item) {
            event.registerItem(capability, provider, item);
        }
    }
}
