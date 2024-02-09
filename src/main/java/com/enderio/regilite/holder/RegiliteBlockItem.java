package com.enderio.regilite.holder;

import com.enderio.regilite.data.RegiliteItemModelProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.function.BiConsumer;

public class RegiliteBlockItem<T extends BlockItem, U extends Block> extends RegiliteItem<T> {
    private RegiliteBlock<U> block;
    protected RegiliteBlockItem(ResourceKey<Item> key) {
        super(key);
        this.setTranslation("");
    }

    public RegiliteBlockItem(ResourceKey<Item> itemResourceKey, RegiliteBlock<U> block) {
        this(itemResourceKey);
        this.block = block;
    }

    public static <U extends Block,T extends BlockItem> RegiliteBlockItem<T,U> createBlockItem(ResourceKey<Item> itemResourceKey, RegiliteBlock<U> block) {
        return new RegiliteBlockItem<>(itemResourceKey, block);
    }

    public RegiliteBlock<U> finishBlockItem() {
        return block;
    }

    @Override
    public RegiliteBlockItem<T,U> setTab(ResourceKey<CreativeModeTab> tab) {
        super.setTab(tab);
        return this;
    }

    @Override
    public RegiliteBlockItem<T,U> setTab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        super.setTab(tab, visibility);
        return this;
    }

    @SafeVarargs
    public final RegiliteBlockItem<T,U> addBlockItemTags(TagKey<Item>... tags) {
        this.ItemTags = Set.of(tags);
        return this;
    }

    @Override
    public RegiliteBlockItem<T,U> setTranslation(String translation) {
        super.setTranslation(translation);
        return this;
    }

    @Override
    public RegiliteBlockItem<T,U> setModelProvider(BiConsumer<RegiliteItemModelProvider, T> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }
}
