package com.enderio.regilite.blocks;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.data.DataGenContext;
import com.enderio.regilite.items.ItemBuilder;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.tags.RegiliteTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.NotImplementedException;
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

    @Nullable
    private BiConsumer<RegiliteBlockLootProvider, T> lootTable = RegiliteBlockLootProvider::dropSelf;

    @Nullable
    private Supplier<Supplier<BlockColor>> blockColorSupplier;

    public BlockBuilder(DeferredBlock<T> holder, RegiliteLang langModule, RegiliteTags tagsModule, RegiliteItems itemsModule) {
        super(holder);
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.itemsModule = itemsModule;
    }

    public BlockBuilder<T> withTranslation(String englishTranslation) {
        langModule.addTranslation(this::getDescriptionId, englishTranslation);
        return this;
    }

    private String getDescriptionId() {
        return get().getDescriptionId();
    }

    @SafeVarargs
    public final BlockBuilder<T> withTags(TagKey<Block>... tags) {
        tagsModule.blocks().addToTags(this::get, tags);
        return this;
    }

    public BlockBuilder<T> withSimpleBlockItem() {
        return withBlockItem(b -> new BlockItem(b, new Item.Properties()), i -> {});
    }

    public BlockBuilder<T> withSimpleBlockItem(Consumer<ItemBuilder<BlockItem>> itemConfigure) {
        return withBlockItem(b -> new BlockItem(b, new Item.Properties()), itemConfigure);
    }

    public BlockBuilder<T> withSimpleBlockItem(Item.Properties properties, Consumer<ItemBuilder<BlockItem>> itemConfigure) {
        return withBlockItem(b -> new BlockItem(b, properties), itemConfigure);
    }

    public <I extends BlockItem> BlockBuilder<T> withBlockItem(Function<T, I> function, Consumer<ItemBuilder<I>> itemConfigure) {
        throw new NotImplementedException();
        //var item = registry.registerBlockItem(getId().getPath(), this, () -> function.apply(this.get()));
        //itemConfigure.accept(item);
        //return this;
    }

    public BlockBuilder<T> withLootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable)  {
        this.lootTable = lootTable;
        return this;
    }

    @ApiStatus.Internal
    public BiConsumer<RegiliteBlockLootProvider, T> lootTable() {
        return lootTable;
    }

    public BlockBuilder<T> withBlockStateProvider(BiConsumer<BlockStateProvider, DataGenContext<Block, T>> blockStateProvider) {
        throw new NotImplementedException();
    }

    public BlockBuilder<T> withBlockColor(@Nullable Supplier<Supplier<BlockColor>> colorSupplier) {
        blockColorSupplier = colorSupplier;
        return this;
    }

    @ApiStatus.Internal
    public Supplier<Supplier<BlockColor>> blockColor() {
        return blockColorSupplier;
    }
}
