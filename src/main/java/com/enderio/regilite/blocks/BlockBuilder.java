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
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BlockBuilder<T extends Block> extends RegiliteBuilder<BlockBuilder<T>, Block, T, DeferredBlock<T>> {

    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final RegiliteItems itemsModule;

    @Nullable
    BiConsumer<RegiliteBlockLootProvider, T> lootTable = RegiliteBlockLootProvider::dropSelf;

    @Nullable
    BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider = (prov, ctx) -> prov.simpleBlock(ctx.get());

    @Nullable
    Supplier<Supplier<BlockColor>> blockColorSupplier;

    private final List<AttachedCapability<?, ?>> attachedCapabilityList = new ArrayList<>();

    public BlockBuilder(DeferredBlock<T> holder, RegiliteLang langModule, RegiliteTags tagsModule, RegiliteItems itemsModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.itemsModule = itemsModule;

        translation(DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public BlockBuilder<T> translation(String englishTranslation) {
        langModule.addBlock(holder, englishTranslation);
        return this;
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
        return createBlockItem(b -> new BlockItem(b, new Item.Properties()), i -> {});
    }

    public BlockBuilder<T> createSimpleBlockItem(Consumer<ItemBuilder<BlockItem>> itemConfigure) {
        return createBlockItem(b -> new BlockItem(b, new Item.Properties()), itemConfigure);
    }

    public BlockBuilder<T> createSimpleBlockItem(Item.Properties properties, Consumer<ItemBuilder<BlockItem>> itemConfigure) {
        return createBlockItem(b -> new BlockItem(b, properties), itemConfigure);
    }

    public <I extends BlockItem> BlockBuilder<T> createBlockItem(Function<T, I> function, Consumer<ItemBuilder<I>> itemConfigure) {
        var item = itemsModule.create(getId().getPath(), () -> function.apply(this.get()));
        itemConfigure.accept(item.removeTranslation());
        return this;
    }

    public BlockBuilder<T> lootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable)  {
        this.lootTable = lootTable;
        return this;
    }

    public BlockBuilder<T> blockState(BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider) {
        this.blockStateProvider = blockStateProvider;
        return this;
    }

    public BlockBuilder<T> blockColor(@Nullable Supplier<Supplier<BlockColor>> colorSupplier) {
        blockColorSupplier = colorSupplier;
        return this;
    }

    public <TCap, TContext> BlockBuilder<T> capability(BlockCapability<TCap, TContext> capability, IBlockCapabilityProvider<TCap, TContext> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    void attachCapabilities(RegisterCapabilitiesEvent event) {
        for (var attachedCapability : attachedCapabilityList) {
            attachedCapability.registerProvider(event, get());
        }
    }

    protected record AttachedCapability<TCap, TContext>(
            BlockCapability<TCap, TContext> capability,
            IBlockCapabilityProvider<TCap, TContext> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, Block block) {
            event.registerBlock(capability, provider, block);
        }
    }
}
