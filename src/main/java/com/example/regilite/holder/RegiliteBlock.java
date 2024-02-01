package com.example.regilite.holder;

import com.example.regilite.data.RegiliteBlockLootProvider;
import com.example.regilite.data.RegiliteDataProvider;
import com.example.regilite.events.IBlockColor;
import com.example.regilite.registry.ItemRegistry;
import com.example.regilite.registry.ITaggable;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteBlock<T extends Block> extends DeferredBlock<T> implements IRegiliteType<Block>, ITaggable<RegiliteBlock<T>, Block> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private Set<TagKey<Block>> blockTags = Set.of();
    @Nullable
    private BiConsumer<RegiliteBlockLootProvider, T>  lootTable = RegiliteBlockLootProvider::dropSelf;
    @Nullable
    private BiConsumer<BlockStateProvider, T> blockStateProvider = BlockStateProvider::simpleBlock;
    @Nullable
    private IBlockColor colorSupplier;
    protected RegiliteBlock(ResourceKey<Block> key) {
        super(key);
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
    }

    public RegiliteBlock<T> setTranslation(String translation) {
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }

    @SafeVarargs
    @Override
    public final RegiliteBlock<T> addTags(TagKey<Block>... tags) {
        blockTags = Set.of(tags);
        return this;
    }

    public Set<TagKey<Block>> getTags() {
        return blockTags;
    }

    public RegiliteBlock<T> setLootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable) {
        this.lootTable = lootTable;
        return this;
    }

    @Nullable
    public BiConsumer<RegiliteBlockLootProvider, T> getLootTable() {
        return lootTable;
    }

    public RegiliteBlock<T> setBlockStateProvider(BiConsumer<BlockStateProvider, T> blockStateProvider) {
        this.blockStateProvider = blockStateProvider;
        return this;
    }

    @Nullable
    public BiConsumer<BlockStateProvider, T> getBlockStateProvider() {
        return blockStateProvider;
    }

    @Nullable
    public IBlockColor getColorSupplier() {
        return colorSupplier;
    }

    public RegiliteBlock<T> setColorSupplier(@Nullable IBlockColor colorSupplier) {
        this.colorSupplier = colorSupplier;
        return this;
    }

    public RegiliteBlockItem<BlockItem, T> createBlockItem(ItemRegistry registry) {
        return registry.registerBlockItem(this);
    }

    public RegiliteBlockItem<BlockItem, T> createBlockItem(ItemRegistry registry, Function<T, ? extends BlockItem> function) {
        return registry.registerBlockItem(getId().getPath(), this, () -> function.apply(this.get()));
    }

    public static <T extends Block> RegiliteBlock<T> createBlock(ResourceLocation key) {
        return createBlock(ResourceKey.create(Registries.BLOCK, key));
    }

    public static <T extends Block> RegiliteBlock<T> createBlock(ResourceKey<Block> key) {
        return new RegiliteBlock<>(key);
    }

    public static class RegiliteLiquidBlock<T extends LiquidBlock, U extends FluidType> extends RegiliteBlock<T> {
        private final RegiliteFluid<U> fluid;

        protected RegiliteLiquidBlock(ResourceKey<Block> key, RegiliteFluid<U> fluid) {
            super(key);
            this.fluid = fluid;
            this.setLootTable(RegiliteBlockLootProvider::noDrop);
            this.setBlockStateProvider((prov, t) -> prov.getVariantBuilder(t)
                    .partialState()
                    .modelForState()
                    .modelFile(prov.models().getExistingFile(new ResourceLocation("water")))
                    .addModel()
            );
        }

        public RegiliteFluid<U> finishLiquidBlock() {
            return fluid;
        }

        public static <B extends LiquidBlock, U extends FluidType> RegiliteLiquidBlock<B, U> createLiquidBlock(ResourceKey<Block> key, RegiliteFluid<U> fluid) {
            return new RegiliteLiquidBlock<>(key, fluid);
        }

        @Override
        public RegiliteLiquidBlock<T,U> setLootTable(BiConsumer<RegiliteBlockLootProvider, T> lootTable) {
            super.setLootTable(lootTable);
            return this;
        }

        @Override
        public RegiliteLiquidBlock<T,U> setBlockStateProvider(BiConsumer<BlockStateProvider, T> blockStateProvider) {
            super.setBlockStateProvider(blockStateProvider);
            return this;
        }

        @Override
        public RegiliteLiquidBlock<T,U> setColorSupplier(@org.jetbrains.annotations.Nullable IBlockColor colorSupplier) {
            super.setColorSupplier(colorSupplier);
            return this;
        }

        @Override
        public RegiliteLiquidBlock<T,U> setTranslation(String translation) {
            super.setTranslation(translation);
            return this;
        }

    }
}
