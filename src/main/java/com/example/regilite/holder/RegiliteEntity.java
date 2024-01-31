package com.example.regilite.holder;

import com.example.regilite.data.RegiliteDataProvider;
import com.example.regilite.registry.ITagagble;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class RegiliteEntity<T extends Entity> extends DeferredHolder<EntityType<? extends Entity>, EntityType<T>> implements ITagagble<EntityType<?>> {
    private final Set<TagKey<EntityType<?>>> entityTags = new HashSet<>();
    private final Supplier<String> supplier = () -> get().getDescriptionId();

    protected RegiliteEntity(ResourceKey<EntityType<? extends Entity>> key) {
        super(key);
        RegiliteDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
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
}
