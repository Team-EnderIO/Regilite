/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.blocks;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.data.DataGenContext;
import com.enderio.regilite.items.ItemBuilder;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BlockBuilder<T extends Block> extends RegiliteBuilder<BlockBuilder<T>, Block, T, DeferredBlock<T>> {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final RegiliteItems itemsModule;

    private Supplier<String> descriptionIdSupplier = this::getDescriptionId;

    @Nullable
    private BiConsumer<RegiliteBlockLootProvider, T> lootTable = RegiliteBlockLootProvider::dropSelf;

    @Nullable
    private BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider = (prov, ctx) -> prov.simpleBlock(ctx.get());

    @Nullable
    private Supplier<Supplier<BlockColor>> blockColorSupplier;

    public BlockBuilder(DeferredBlock<T> holder, RegiliteLang langModule, RegiliteTags tagsModule, RegiliteItems itemsModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.itemsModule = itemsModule;

        // Default translation
        translation(DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public BlockBuilder<T> translation(String englishTranslation) {
        langModule.add(descriptionIdSupplier, englishTranslation);
        return this;
    }

    // Do not call directly, use descriptionIdSupplier
    @Deprecated
    private String getDescriptionId() {
        return get().getDescriptionId();
    }

    public BlockBuilder<T> tag(TagKey<Block> tag) {
        tagsModule.blocks().tag(tag).add(this::get);
        return this;
    }

    @SafeVarargs
    public final BlockBuilder<T> tags(TagKey<Block>... tags) {
        tagsModule.blocks().addToTags(this::get, tags);
        return this;
    }

    // TODO: more permutations, based on those available in RegiliteItems.
    public BlockBuilder<T> createSimpleBlockItem() {
        return withBlockItem(b -> new BlockItem(b, new Item.Properties()), i -> {});
    }

    public BlockBuilder<T> createSimpleBlockItem(Consumer<ItemBuilder<BlockItem>> itemConfigure) {
        return withBlockItem(b -> new BlockItem(b, new Item.Properties()), itemConfigure);
    }

    public BlockBuilder<T> createSimpleBlockItem(Item.Properties properties, Consumer<ItemBuilder<BlockItem>> itemConfigure) {
        return withBlockItem(b -> new BlockItem(b, properties), itemConfigure);
    }

    public <I extends BlockItem> BlockBuilder<T> withBlockItem(Function<T, I> function, Consumer<ItemBuilder<I>> itemConfigure) {
        var item = itemsModule.create(getId().getPath(), () -> function.apply(this.get()));
        itemConfigure.accept(item);
        return this;
    }

    public BlockBuilder<T> lootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable)  {
        this.lootTable = lootTable;
        return this;
    }

    @ApiStatus.Internal
    public BiConsumer<RegiliteBlockLootProvider, T> lootTable() {
        return lootTable;
    }

    public BlockBuilder<T> blockStateProvider(BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider) {
        this.blockStateProvider = blockStateProvider;
        return this;
    }

    @ApiStatus.Internal
    public BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider() {
        return blockStateProvider;
    }

    public BlockBuilder<T> blockColor(@Nullable Supplier<Supplier<BlockColor>> colorSupplier) {
        blockColorSupplier = colorSupplier;
        return this;
    }

    @ApiStatus.Internal
    public Supplier<Supplier<BlockColor>> blockColor() {
        return blockColorSupplier;
    }
}
