/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.holder;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.data.DataGenContext;
import com.enderio.regilite.registry.ITagagble;
import com.enderio.regilite.data.RegiliteItemModelProvider;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegiliteItem<T extends Item> extends DeferredItem<T> implements ITagagble<Item>, RegiliteHolder<RegiliteItem<T>> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private final Regilite regilite;
    protected Set<TagKey<Item>> ItemTags = new HashSet<>();
    protected Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> tab = new HashMap<>();
    @Nullable
    protected BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider = (prov, ctx) -> prov.basicItem(ctx.get());
    protected Supplier<Supplier<ItemColor>> colorSupplier;
    protected List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    protected RegiliteItem(ResourceKey<Item> key, Regilite regilite) {
        super(key);
        this.regilite = regilite;
        regilite.addTranslation(supplier, DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public RegiliteItem<T> withTranslation(String translation) {
        regilite.addTranslation(supplier, translation);
        return this;
    }

    @SafeVarargs
    public final RegiliteItem<T> withTags(TagKey<Item>... tags) {
        ItemTags.addAll(new HashSet<>(List.of(tags)));
        return this;
    }

    public Set<TagKey<Item>> getTags() {
        return ItemTags;
    }

    public RegiliteItem<T> withTab(ResourceKey<CreativeModeTab> tab) {
        this.tab.put(tab, output -> output.accept(new ItemStack(this.get())));
        return this;
    }

    public RegiliteItem<T> withTab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        this.tab.put(tab, output -> output.accept(new ItemStack(this.get()), visibility));
        return this;
    }

    public RegiliteItem<T> withTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTab.Output> output) {
        this.tab.put(tab, output);
        return this;
    }

    public Map<ResourceKey<CreativeModeTab>, Consumer<CreativeModeTab.Output>> getTab() {
        return tab;
    }

    public RegiliteItem<T> setModelProvider(BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }

    public BiConsumer<RegiliteItemModelProvider, DataGenContext<Item, T>> getModelProvider() {
        return modelProvider;
    }

    public Supplier<Supplier<ItemColor>> getColorSupplier() {
        return colorSupplier;
    }

    public RegiliteItem<T> setColorSupplier(Supplier<Supplier<ItemColor>> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public <TCap, TContext> RegiliteItem<T> withCapability(ItemCapability<TCap, TContext> capability, ICapabilityProvider<ItemStack, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    @ApiStatus.Internal
    public void registerCapabilityProviders(RegisterCapabilitiesEvent event) {
        for (AttachedCapability<T, ?, ?> capabilityProvider : attachedCapabilityList) {
            capabilityProvider.registerProvider(event, value());
        }
    }

    protected record AttachedCapability<T extends Item, TCap, TContext>(
            ItemCapability<TCap, TContext> capability,
            ICapabilityProvider<ItemStack, TContext, TCap> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, Item item) {
            event.registerItem(capability, provider, item);
        }
    }

    /**
     * Creates a new {@link DeferredHolder} targeting the specified {@link Item}.
     *
     * @param <T> The type of the target {@link Item}.
     * @param key The resource key of the target {@link Item}.
     */
    public static <T extends Item> RegiliteItem<T> createItem(ResourceKey<Item> key, Regilite regilite) {
        return new RegiliteItem<>(key, regilite);
    }
}
