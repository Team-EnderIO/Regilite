package com.enderio.regilite.registry;

import com.enderio.regilite.events.IScreenConstructor;
import com.enderio.regilite.events.ScreenEvents;
import com.enderio.regilite.holder.RegiliteMenu;
import com.enderio.regilite.mixin.DeferredRegisterAccessor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class MenuRegistry extends DeferredRegister<MenuType<?>> {
    protected MenuRegistry(String namespace) {
        super(BuiltInRegistries.MENU.key(), namespace);
    }

    protected <I extends AbstractContainerMenu> RegiliteMenu<I> createMenuHolder(ResourceKey<? extends Registry<MenuType<? extends AbstractContainerMenu>>> registryKey, ResourceLocation key) {
        return RegiliteMenu.createMenu(ResourceKey.create(registryKey, key));
    }

    public <I extends AbstractContainerMenu> RegiliteMenu<I> registerMenu(String name, Function<ResourceLocation, ? extends MenuType<I>> func) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        RegiliteMenu<I> ret = createMenuHolder(this.getRegistryKey(), key);

        if (((DeferredRegisterAccessor<MenuType<? extends AbstractContainerMenu>>) this).getEntries().putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <I extends AbstractContainerMenu> RegiliteMenu<I> registerMenu(String name, Supplier<? extends MenuType<I>> sup) {
        return registerMenu(name, key -> sup.get());
    }

    public <I extends AbstractContainerMenu> RegiliteMenu<I> registerMenu(String name, IContainerFactory<I> sup) {
        return registerMenu(name, () -> new MenuType<>(sup, FeatureFlags.DEFAULT_FLAGS));
    }

    public <I extends AbstractContainerMenu> RegiliteMenu<I> registerMenu(String name, IContainerFactory<I> sup, IScreenConstructor<I, ? extends AbstractContainerScreen<I>> screen) {
        return registerMenu(name, sup).setScreenConstructor(screen);
    }

    public static MenuRegistry createRegistry(String modid) {
        return new MenuRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(new ScreenEvents(this)::screenEvent);
        }
    }
}
