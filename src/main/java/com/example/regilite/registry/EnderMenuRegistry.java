package com.example.regilite.registry;

import com.example.regilite.events.IScreenConstructor;
import com.example.regilite.events.ScreenEvents;
import com.example.regilite.mixin.DeferredRegisterAccessor;
import net.minecraft.client.gui.screens.MenuScreens;
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

public class EnderMenuRegistry extends DeferredRegister<MenuType<?>> {
    protected EnderMenuRegistry(String namespace) {
        super(BuiltInRegistries.MENU.key(), namespace);
    }

    protected <I extends AbstractContainerMenu> EnderDeferredMenu<I> createMenuHolder(ResourceKey<? extends Registry<MenuType<? extends AbstractContainerMenu>>> registryKey, ResourceLocation key) {
        return EnderDeferredMenu.createMenu(ResourceKey.create(registryKey, key));
    }

    public <I extends AbstractContainerMenu> EnderDeferredMenu<I> registerMenu(String name, Function<ResourceLocation, ? extends MenuType<I>> func) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(func);
        final ResourceLocation key = new ResourceLocation(getNamespace(), name);

        EnderDeferredMenu<I> ret = createMenuHolder(this.getRegistryKey(), key);

        if (((DeferredRegisterAccessor<MenuType<? extends AbstractContainerMenu>>) this).getEntries().putIfAbsent(ret, () -> func.apply(key)) != null) {
            throw new IllegalArgumentException("Duplicate registration " + name);
        }

        return ret;
    }

    public <I extends AbstractContainerMenu> EnderDeferredMenu<I> registerMenu(String name, Supplier<? extends MenuType<I>> sup) {
        return registerMenu(name, key -> sup.get());
    }

    public <I extends AbstractContainerMenu> EnderDeferredMenu<I> registerMenu(String name, IContainerFactory<I> sup) {
        return registerMenu(name, () -> new MenuType<>(sup, FeatureFlags.DEFAULT_FLAGS));
    }

    public <I extends AbstractContainerMenu> EnderDeferredMenu<I> registerMenu(String name, IContainerFactory<I> sup, IScreenConstructor<I, ? extends AbstractContainerScreen<I>> screen) {
        return registerMenu(name, sup).setScreenConstructor(screen);
    }

    public static EnderMenuRegistry createRegistry(String modid) {
        return new EnderMenuRegistry(modid);
    }

    @Override
    public void register(IEventBus bus) {
        super.register(bus);
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(new ScreenEvents(this)::screenEvent);
        }
    }
}
