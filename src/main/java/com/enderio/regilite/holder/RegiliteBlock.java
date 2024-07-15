/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.holder;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.data.DataGenContext;
import com.enderio.regilite.registry.ITagagble;
import com.enderio.regilite.registry.ItemRegistry;
import com.enderio.regilite.data.RegiliteBlockLootProvider;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteBlock<T extends Block> extends DeferredBlock<T> implements ITagagble<Block> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private final Regilite regilite;
    private Set<TagKey<Block>> blockTags = Set.of();
    @Nullable
    private BiConsumer<RegiliteBlockLootProvider, T> lootTable = RegiliteBlockLootProvider::dropSelf;
    @Nullable
    private BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider = (prov, ctx) -> prov.simpleBlock(ctx.get());
    @Nullable
    private Supplier<Supplier<BlockColor>> colorSupplier;
    protected RegiliteBlock(ResourceKey<Block> key, Regilite regilite) {
        super(key);
        this.regilite = regilite;
        regilite.addTranslation(supplier, DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public RegiliteBlock<T> withTranslation(String translation) {
        regilite.addTranslation(supplier, translation);
        return this;
    }

    @SafeVarargs
    public final RegiliteBlock<T> withTags(TagKey<Block>... tags) {
        blockTags = new HashSet<>(List.of(tags));
        return this;
    }

    public Set<TagKey<Block>> getTags() {
        return blockTags;
    }

    public RegiliteBlock<T> withLootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    @Nullable
    public BiConsumer<RegiliteBlockLootProvider, T> getLootTable() {
        return lootTable;
    }

    public RegiliteBlock<T> setBlockStateProvider(BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider) {
        this.blockStateProvider = blockStateProvider;
        return this;
    }

    @Nullable
    public BiConsumer<BlockStateProvider, DataGenContext<Block, T>> getBlockStateProvider() {
        return blockStateProvider;
    }

    @Nullable
    public Supplier<Supplier<BlockColor>> getColorSupplier() {
        return colorSupplier;
    }

    public RegiliteBlock<T> withBlockColor(@Nullable Supplier<Supplier<BlockColor>> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public RegiliteBlock<T> withBlockItem(ItemRegistry registry, Consumer<RegiliteItem<BlockItem>> itemConfigure) {
        var item = registry.registerBlockItem(this);
        itemConfigure.accept(item);
        return this;
    }

    public RegiliteBlock<T> withBlockItem(ItemRegistry registry, Function<T, ? extends BlockItem> function,
                                          Consumer<RegiliteItem<? extends BlockItem>> itemConfigure) {
        var item = registry.registerBlockItem(getId().getPath(), this, () -> function.apply(this.get()));
        itemConfigure.accept(item);
        return this;
    }

    public static <T extends Block> RegiliteBlock<T> createBlock(ResourceKey<Block> key, Regilite regilite) {
        return new RegiliteBlock<>(key, regilite);
    }
}
