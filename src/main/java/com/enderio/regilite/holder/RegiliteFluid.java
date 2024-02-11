package com.enderio.regilite.holder;

import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.registry.ITagagble;
import com.enderio.regilite.registry.ItemRegistry;
import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteFluid<T extends FluidType> extends DeferredHolder<FluidType, T> implements ITagagble<Fluid> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private Set<TagKey<Fluid>> FluidTags = Set.of();
    private DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowingFluid;
    private DeferredHolder<Fluid, BaseFlowingFluid.Source> sourceFluid;
    private RegiliteBlock.RegiliteLiquidBlock<? extends LiquidBlock, T> block;
    private RegiliteItem.RegiliteBucketItem<? extends BucketItem, T> bucket;
    private final BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(this, this::getSource, this::getFlowing).block(this::getBlock).bucket(this::getBucket);
    private Supplier<Supplier<RenderType>> renderTypeSupplier = () -> null;

    protected RegiliteFluid(ResourceKey<FluidType> key) {
        super(key);
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public static <I extends FluidType> RegiliteFluid<I> createHolder(ResourceKey<FluidType> fluidTypeResourceKey) {
        return new RegiliteFluid<>(fluidTypeResourceKey);
    }

    public RegiliteFluid<T> createFluid(DeferredRegister<Fluid> register, Consumer<BaseFlowingFluid.Properties> consumer) {
        consumer.accept(properties);
        this.flowingFluid = register.register("fluid_" + getId().getPath() + "_flowing", () -> new BaseFlowingFluid.Flowing(properties));
        this.sourceFluid = register.register("fluid_" + getId().getPath() + "_still", () -> new BaseFlowingFluid.Source(properties));
        return this;
    }
    public RegiliteFluid<T> createFluid(DeferredRegister<Fluid> register) {
        return this.createFluid(register, properties1 -> {});
    }



    public RegiliteBlock.RegiliteLiquidBlock<? extends LiquidBlock, T> withBlock(BlockRegistry registry, Function<Supplier<BaseFlowingFluid.Flowing>, ? extends LiquidBlock> supplier) {
        this.block = registry.registerLiquidBlock(getId().getPath(), () -> supplier.apply(this.flowingFluid), this);
        return this.block;
    }

    public RegiliteItem.RegiliteBucketItem<? extends BucketItem, T> withBucket(ItemRegistry registry, Function<Supplier<BaseFlowingFluid.Source>, ? extends BucketItem> supplier) {
        this.bucket = registry.registerBucket(getId().getPath() + "_bucket", () -> supplier.apply(this.sourceFluid), this);
        return this.bucket;
    }

    public BaseFlowingFluid.Source getSource() {
        return sourceFluid.get();
    }

    public BaseFlowingFluid.Flowing getFlowing() {
        return flowingFluid.get();
    }

    public BucketItem getBucket() {
        return bucket.get();
    }

    public LiquidBlock getBlock() {
        return block.get();
    }

    @SafeVarargs
    public final RegiliteFluid<T> addFluidTags(TagKey<Fluid>... tags) {
        this.FluidTags = Set.of(tags);
        return this;
    }

    public Set<TagKey<Fluid>> getTags() {
        return FluidTags;
    }

    public RegiliteFluid<T> setTranslation(String translation) {
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }

    public Supplier<RenderType> getRenderType() {
        return renderTypeSupplier.get();
    }

    public RegiliteFluid<T> setRenderType(Supplier<Supplier<RenderType>> renderTypeSupplier) {
        this.renderTypeSupplier = renderTypeSupplier;
        return this;
    }
}
