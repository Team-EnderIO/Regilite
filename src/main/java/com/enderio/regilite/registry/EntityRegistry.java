package com.enderio.regilite.registry;

import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.data.RegiliteTagProvider;
import com.enderio.regilite.events.BlockEntityRendererEvents;
import com.enderio.regilite.events.EntityRendererEvents;
import com.enderio.regilite.holder.RegiliteEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
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

        RegiliteEntity<T> ret = RegiliteEntity.createEntity(ResourceKey.create(getRegistryKey(), key));

        var entries = DeferredRegistryReflect.getEntries(this);
        if (entries.putIfAbsent(ret, () -> func.apply(key)) != null) {
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

    public static EntityRegistry create(String modid) {
        return new EntityRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        onGatherData(bus);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(new EntityRendererEvents(this)::registerER);
        }
    }

    private void  onGatherData(IEventBus bus) {
        RegiliteDataProvider provider = RegiliteDataProvider.register(getNamespace(), bus);
        provider.addServerSubProvider((packOutput, existingFileHelper, lookup) -> new RegiliteTagProvider<>(packOutput, this.getRegistryKey(), b -> b.builtInRegistryHolder().key(), lookup, getNamespace(), existingFileHelper, this));
    }
}
