package com.enderio.regilite.menus;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.RegiliteModuleEvents;
import com.enderio.regilite.RegiliteRegistryModule;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public class RegiliteMenus implements RegiliteRegistryModule<MenuType<?>, DeferredRegister<MenuType<?>>>, RegiliteModuleEvents {

    private final DeferredRegister<MenuType<?>> deferredRegister;

    final ObjectList<MenuBuilder<?>> menus = new ObjectArrayList<>();

    protected RegiliteMenus(DeferredRegister<MenuType<?>> deferredRegister) {
        this.deferredRegister = deferredRegister;
    }

    @ApiStatus.Internal
    public static RegiliteMenus create(Regilite regilite) {
        return new RegiliteMenus(DeferredRegister.create(Registries.MENU, regilite.modId()));
    }

    public <T extends AbstractContainerMenu> MenuBuilder<T> create(String name, Supplier<MenuType<T>> menuSupplier) {
        var holder = deferredRegister.register(name, menuSupplier);
        var builder = new MenuBuilder<>(holder);
        menus.add(builder);
        return builder;
    }

    public <T extends AbstractContainerMenu> MenuBuilder<T> create(String name, IContainerFactory<T> factory) {
        return create(name, () -> new MenuType<>(factory, FeatureFlags.DEFAULT_FLAGS));
    }

    public <T extends AbstractContainerMenu> MenuBuilder<T> create(String name, IContainerFactory<T> factory, Supplier<IScreenConstructor<T, ? extends AbstractContainerScreen<T>>> screenFactory) {
        return create(name, factory).screen(screenFactory);
    }

    public void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(new RegiliteClientMenus(this));
        }
    }

    @Override
    public ResourceKey<Registry<MenuType<?>>> registry() {
        return Registries.MENU;
    }

    @Override
    public DeferredRegister<MenuType<?>> deferredRegister() {
        return deferredRegister;
    }
}
