package com.enderio.regilite.blockentities;

import com.enderio.regilite.RegiliteBuilder;
import com.enderio.regilite.tags.RegiliteTags;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity>
        extends RegiliteBuilder<BlockEntityBuilder<T>, BlockEntityType<?>, BlockEntityType<T>, DeferredHolder<BlockEntityType<?>, BlockEntityType<T>>> {

    private final RegiliteTags tagsModule;

    protected Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> rendererFactory;

    private final List<AttachedCapability<T, ?, ?>> attachedCapabilityList = new ArrayList<>();

    protected BlockEntityBuilder(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder, RegiliteTags tagsModule) {
        super(holder);
        this.tagsModule = tagsModule;
    }

    public BlockEntityBuilder<T> tag(TagKey<BlockEntityType<?>> tag) {
        tagsModule.blockEntityTypes().tag(tag).add(this::get);
        return this;
    }

    @SafeVarargs
    public final BlockEntityBuilder<T> tags(TagKey<BlockEntityType<?>>... tags) {
        tagsModule.blockEntityTypes().addToTags(this::get, tags);
        return this;
    }

    public BlockEntityBuilder<T> renderer(Supplier<Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> rendererFactory) {
        this.rendererFactory = rendererFactory;
        return this;
    }

    public <TCap, TContext> BlockEntityBuilder<T> capability(BlockCapability<TCap, TContext> capability, ICapabilityProvider<? super T, TContext, TCap> provider) {
        attachedCapabilityList.add(new AttachedCapability<>(capability, provider));
        return this;
    }

    void attachCapabilities(RegisterCapabilitiesEvent event) {
        for (var attachedCapability : attachedCapabilityList) {
            attachedCapability.registerProvider(event, get());
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
