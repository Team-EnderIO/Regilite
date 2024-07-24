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

    protected ItemBuilder(DeferredItem<T> holder, RegiliteLang langModule, RegiliteTags tagsModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;
    }

    public ItemBuilder<T> withTranslation(String englishTranslation) {
        langModule.addTranslation(this::getDescriptionId, englishTranslation);
        return this;
    }

    private String getDescriptionId() {
        return get().getDescriptionId();
    }

    @SafeVarargs
    public final ItemBuilder<T> withTags(TagKey<Item>... tags) {
        //tagsModule.registry(Registries.ITEM).addToTags(this::get, tags);
        return this;
    }

    public final ItemBuilder<T> withTab(ResourceKey<CreativeModeTab> tab) {
        throw new NotImplementedException();
    }

    public final ItemBuilder<T> withTab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        throw new NotImplementedException();
    }

    public final ItemBuilder<T> withTab(ResourceKey<CreativeModeTab> tab, Consumer<CreativeModeTab.Output> output) {
        throw new NotImplementedException();
    }
}
