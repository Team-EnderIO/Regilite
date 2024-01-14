package com.example.regilite.registry;

import com.example.regilite.data.RegiliteDataProvider;
import com.example.regilite.data.RegiliteTagProvider;
import com.example.regilite.holder.RegiliteEntity;
import com.example.regilite.mixin.DeferredRegisterAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntityRegistry extends DeferredRegister<EntityType<?>> {
    protected EntityRegistry(String namespace) {
        super(BuiltInRegistries.ENTITY_TYPE.key(), namespace);
    }

    private <T extends Entity> RegiliteEntity<T> registerEntity(String name, Function<ResourceLocation, EntityType<T>> func) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        RegiliteEntity<T> ret = createEntityHolder(getRegistryKey(), key);

        if (((DeferredRegisterAccessor<EntityType<?>>)this).getEntries().putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <T extends Entity> RegiliteEntity<T> registerEntity(String name, EntityType.EntityFactory<T> sup, MobCategory category) {
        return this.registerEntity(name, () -> EntityType.Builder.of(sup, category).build(name));
    }

    public <T extends Entity> RegiliteEntity<T> registerEntity(String name, Supplier<EntityType<T>> supplier) {
        return this.registerEntity(name, key -> supplier.get());
    }

    protected <T extends Entity> RegiliteEntity<T> createEntityHolder(ResourceKey<? extends Registry<EntityType<?>>> registryKey, ResourceLocation key) {
        return new RegiliteEntity<>(ResourceKey.create(registryKey, key));
    }

    public static EntityRegistry create(String modid) {
        return new EntityRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        onGatherData(bus);
    }

    private void  onGatherData(IEventBus bus) {
        RegiliteDataProvider provider = RegiliteDataProvider.register(getNamespace(), bus);
        provider.addServerSubProvider((packOutput, existingFileHelper, lookup) -> new RegiliteTagProvider<>(packOutput, this.getRegistryKey(), b -> b.builtInRegistryHolder().key(), lookup, getNamespace(), existingFileHelper, this));
    }
}
