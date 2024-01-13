package com.example.regilite.registry;

import com.example.regilite.data.EnderItemModelProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Set;
import java.util.function.BiConsumer;

public class EnderDeferredBlockItem<T extends BlockItem, U extends Block> extends EnderDeferredItem<T> {
    private EnderDeferredBlock<U> block;
    protected EnderDeferredBlockItem(ResourceKey<Item> key) {
        super(key);
        this.setTranslation("");
    }

    public EnderDeferredBlockItem(ResourceKey<Item> itemResourceKey, EnderDeferredBlock<U> block) {
        this(itemResourceKey);
        this.block = block;
    }

    public static <U extends Block,T extends BlockItem> EnderDeferredBlockItem<T,U> createBlockItem(ResourceKey<Item> itemResourceKey, EnderDeferredBlock<U> block) {
        return new EnderDeferredBlockItem<>(itemResourceKey, block);
    }

    public EnderDeferredBlock<U> finishBlockItem() {
        return block;
    }

    @Override
    public EnderDeferredBlockItem<T,U> setTab(ResourceKey<CreativeModeTab> tab) {
        super.setTab(tab);
        return this;
    }

    @Override
    public EnderDeferredBlockItem<T,U> setTab(ResourceKey<CreativeModeTab> tab, CreativeModeTab.TabVisibility visibility) {
        super.setTab(tab, visibility);
        return this;
    }

    @SafeVarargs
    public final EnderDeferredBlockItem<T,U> addBlockItemTags(TagKey<Item>... tags) {
        this.ItemTags = Set.of(tags);
        return this;
    }

    @Override
    public EnderDeferredBlockItem<T,U> setTranslation(String translation) {
        super.setTranslation(translation);
        return this;
    }

    @Override
    public EnderDeferredBlockItem<T,U> setModelProvider(BiConsumer<EnderItemModelProvider, T> modelProvider) {
        this.modelProvider = modelProvider;
        return this;
    }
}