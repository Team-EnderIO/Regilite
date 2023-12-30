package com.example.examplemod.registry;

import com.example.examplemod.data.EnderDataProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class EnderDeferredEntity<T extends Entity> extends DeferredHolder<EntityType<? extends Entity>, EntityType<T>> implements ITagagble<EntityType<?>>{
    private final Set<TagKey<EntityType<?>>> entityTags = new HashSet<>();
    private final Supplier<String> supplier = () -> get().getDescriptionId();

    protected EnderDeferredEntity(ResourceKey<EntityType<? extends Entity>> key) {
        super(key);
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, StringUtils.capitalize(getId().getPath().replace('_', ' ')));
    }

    @Override
    public Set<TagKey<EntityType<?>>> getTags() {
        return null;
    }

    public static <T extends Entity> EnderDeferredEntity<T> createBlockEntity(ResourceKey<EntityType<? extends Entity>> key) {
        return new EnderDeferredEntity<>(key);
    }

    @SafeVarargs
    public final EnderDeferredEntity<T> addEntityTags(TagKey<EntityType<?>>... tags) {
        this.entityTags.addAll(Set.of(tags));
        return this;
    }

    public EnderDeferredEntity<T> setTranslation(String translation) {
        EnderDataProvider.getInstance(getId().getNamespace()).addTranslation(supplier, translation);
        return this;
    }
}
