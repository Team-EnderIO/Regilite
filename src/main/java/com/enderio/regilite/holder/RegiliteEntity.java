package com.enderio.regilite.holder;

import com.enderio.regilite.registry.ITagagble;
import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.util.NonNullFunction;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class RegiliteEntity<T extends Entity> extends DeferredHolder<EntityType<? extends Entity>, EntityType<T>> implements ITagagble<EntityType<?>> {
    private final Set<TagKey<EntityType<?>>> entityTags = new HashSet<>();
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private Supplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer = null;

    protected RegiliteEntity(ResourceKey<EntityType<? extends Entity>> key) {
        super(key);
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    @Override
    public Set<TagKey<EntityType<?>>> getTags() {
        return null;
    }

    public static <T extends Entity> RegiliteEntity<T> createEntity(ResourceKey<EntityType<? extends Entity>> key) {
        return new RegiliteEntity<>(key);
    }

    @SafeVarargs
    public final RegiliteEntity<T> addEntityTags(TagKey<EntityType<?>>... tags) {
        this.entityTags.addAll(Set.of(tags));
        return this;
    }

    public RegiliteEntity<T> setTranslation(String translation) {
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }

    public RegiliteEntity<T> setRenderer(Supplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer) {
        this.renderer = renderer;
        return this;
    }

    public Supplier<NonNullFunction<EntityRendererProvider.Context, EntityRenderer<? super T>>> getRenderer() {
        return renderer;
    }
}
