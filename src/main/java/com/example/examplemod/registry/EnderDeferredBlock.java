package com.example.examplemod.registry;

import com.example.examplemod.data.EnderBlockLootProvider;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EnderDeferredBlock<T extends Block> extends DeferredBlock<T> implements ITranslatable{
    private String translation = StringUtils.capitalize(getId().getPath().replace('_', ' '));
    private Set<TagKey<Block>> blockTags = Set.of();
    @Nullable
    private BiConsumer<EnderBlockLootProvider, T>  lootTable = EnderBlockLootProvider::dropSelf;
    @Nullable
    private BiConsumer<BlockStateProvider, T> blockStateProvider = BlockStateProvider::simpleBlock;
    @Nullable
    private Supplier<BlockColor> colorSupplier;
    @Nullable
    private EnderItemRegistry registry;
    protected EnderDeferredBlock(ResourceKey<Block> key) {
        super(key);
    }

    public EnderDeferredBlock<T> setTranslation(String translation) {
        this.translation = translation;
        return this;
    }

    @Override
    public String getTranslation() {
        return translation;
    }

    @SafeVarargs
    public final EnderDeferredBlock<T> addBlockTags(TagKey<Block>... tags) {
        blockTags = Set.of(tags);
        return this;
    }

    public Set<TagKey<Block>> getBlockTags() {
        return blockTags;
    }

    public EnderDeferredBlock<T> setLootTable(BiConsumer<EnderBlockLootProvider, T> lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    @Nullable
    public BiConsumer<EnderBlockLootProvider, T> getLootTable() {
        return lootTable;
    }

    public EnderDeferredBlock<T> setBlockStateProvider(BiConsumer<BlockStateProvider, T> blockStateProvider) {
        this.blockStateProvider = blockStateProvider;
        return this;
    }

    @Nullable
    public BiConsumer<BlockStateProvider, T> getBlockStateProvider() {
        return blockStateProvider;
    }

    @Nullable
    public Supplier<BlockColor> getColorSupplier() {
        return colorSupplier;
    }

    public EnderDeferredBlock<T> setColorSupplier(@Nullable Supplier<BlockColor> colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public EnderDeferredBlock<T> setRegistry(@Nullable EnderItemRegistry registry) {
        this.registry = registry;
        return this;
    }

    public EnderDeferredBlockItem<BlockItem, T> createBlockItem() {
        return registry.registerBlockItem(this);
    }

    public EnderDeferredBlockItem<BlockItem, T> createBlockItem(Function<T, ? extends BlockItem> function) {
        return registry.registerBlockItem(getId().getPath(), this, () -> function.apply(this.get()));
    }

    public static <T extends Block> EnderDeferredBlock<T> createBlock(ResourceLocation key) {
        return createBlock(ResourceKey.create(Registries.BLOCK, key));
    }

    public static <T extends Block> EnderDeferredBlock<T> createBlock(ResourceKey<Block> key) {
        return new EnderDeferredBlock<>(key);
    }
}
