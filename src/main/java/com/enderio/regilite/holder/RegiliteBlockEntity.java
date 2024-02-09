package com.enderio.regilite.holder;

import com.enderio.regilite.registry.ITagagble;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class RegiliteBlockEntity<T extends BlockEntity> extends DeferredHolder<BlockEntityType<? extends BlockEntity>, BlockEntityType<T>> implements ITagagble<BlockEntityType<?>> {
    protected Set<TagKey<BlockEntityType<?>>> BlockEntityTags = new HashSet<>();
    protected Supplier<BlockEntityRendererProvider<T>> renderer;

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

    public RegiliteBlockEntity<T> setRenderer(Supplier<BlockEntityRendererProvider<T>> renderer) {
        this.renderer = renderer;
        return this;
    }

    public Supplier<BlockEntityRendererProvider<T>> getRenderer() {
        return renderer;
    }
}
