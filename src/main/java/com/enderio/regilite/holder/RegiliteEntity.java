/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.holder;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.utils.DefaultTranslationUtility;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;
import java.util.function.Supplier;

public class RegiliteEntity<T extends Entity> extends DeferredHolder<EntityType<? extends Entity>, EntityType<T>> implements RegiliteHolder<RegiliteEntity<T>> {
    private final Supplier<String> supplier = () -> get().getDescriptionId();
    private final Regilite regilite;
    private Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer = null;

    protected RegiliteEntity(ResourceKey<EntityType<? extends Entity>> key, Regilite regilite) {
        super(key);
        this.regilite = regilite;

        withTranslation(DefaultTranslationUtility.getDefaultTranslationFrom(getId().getPath()));
    }

    public static <T extends Entity> RegiliteEntity<T> createEntity(ResourceKey<EntityType<? extends Entity>> key, Regilite regilite) {
        return new RegiliteEntity<>(key, regilite);
    }

    @SafeVarargs
    public final RegiliteEntity<T> withTags(TagKey<EntityType<?>>... tags) {
        regilite.tags().entityTypes().addToTags(this, tags);
        return this;
    }

    public RegiliteEntity<T> withTranslation(String translation) {
        regilite.lang().addEntity(this, translation);
        return this;
    }

    public RegiliteEntity<T> withRenderer(Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> renderer) {
        this.renderer = renderer;
        return this;
    }

    public Supplier<Function<EntityRendererProvider.Context, EntityRenderer<? super T>>> getRenderer() {
        return renderer;
    }
}
