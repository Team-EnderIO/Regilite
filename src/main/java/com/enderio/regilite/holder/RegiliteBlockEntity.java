package com.enderio.regilite.holder;

import com.enderio.regilite.registry.ITagagble;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.NonNullFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegiliteBlockEntity<T extends BlockEntity> extends DeferredHolder<BlockEntityType<? extends BlockEntity>, BlockEntityType<T>> implements ITagagble<BlockEntityType<?>> {
    protected Set<TagKey<BlockEntityType<?>>> BlockEntityTags = new HashSet<>();
    protected Supplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer;

    protected List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    /**
     * Creates a new DeferredHolder with a ResourceKey.
     *
     * <p>Attempts to bind immediately if possible.
     *
     * @param key The resource key of the target object.
     * @see #create(ResourceKey, ResourceLocation)
     * @see #create(ResourceLocation, ResourceLocation)
     * @see #create(ResourceKey)
     */
    protected RegiliteBlockEntity(ResourceKey<BlockEntityType<? extends BlockEntity>> key) {
        super(key);
    }

    public static <T extends BlockEntity> RegiliteBlockEntity<T> createBlockEntity(ResourceKey<BlockEntityType<? extends BlockEntity>> key) {
        return new RegiliteBlockEntity<>(key);
    }

    @Nullable
    public T create(BlockPos pos, BlockState state) {
        return this.get().create(pos, state);
    }

    @Override
    public Set<TagKey<BlockEntityType<?>>> getTags() {
        return this.BlockEntityTags;
    }

    @SafeVarargs
    public final RegiliteBlockEntity<T> addBlockEntityTagsTags(TagKey<BlockEntityType<?>>... tags) {
        BlockEntityTags.addAll(Set.of(tags));
        return this;
    }

    public RegiliteBlockEntity<T> setRenderer(Supplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer) {
        this.renderer = renderer;
        return this;
    }

    public Supplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> getRenderer() {
        return renderer;
    }

    public <TCap, TContext> RegiliteBlockEntity<T> addCapability(BlockCapability<TCap, TContext> capability, ICapabilityProvider<? super T, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    // Allows wrapping common holder builder methods into other methods and applying them.
    public RegiliteBlockEntity<T> apply(Consumer<RegiliteBlockEntity<T>> applicator) {
        applicator.accept(this);
        return this;
    }

    @ApiStatus.Internal
    public void registerCapabilityProviders(RegisterCapabilitiesEvent event) {
        for (AttachedCapability<T, ?, ?> capabilityProvider : attachedCapabilityList) {
            capabilityProvider.registerProvider(event, value());
        }
    }

    protected record AttachedCapability<T extends BlockEntity, TCap, TContext>(
            BlockCapability<TCap, TContext> capability,
            ICapabilityProvider<? super T, TContext, TCap> provider) {

        private void registerProvider(RegisterCapabilitiesEvent event, BlockEntityType<T> type) {
            event.registerBlockEntity(capability, type, provider);
        }
    }
}
