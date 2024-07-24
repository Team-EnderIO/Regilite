/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.items;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Consumer;

public class ItemBuilder<T extends Item> extends RegiliteBuilder<ItemBuilder<T>, Item, T, DeferredItem<T>> {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;

    public ItemBuilder(DeferredItem<T> holder, RegiliteLang langModule, RegiliteTags tagsModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;
    }

    public ItemBuilder<T> withTranslation(String englishTranslation) {
        langModule.add(this::getDescriptionId, englishTranslation);
        return this;
    }

    private String getDescriptionId() {
        return get().getDescriptionId();
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
        throw new NotImplementedException();
    }

    public final ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        throw new NotImplementedException();
    }

    public final ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTab.Output> output) {
        throw new NotImplementedException();
    }
}
