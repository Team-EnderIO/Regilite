package com.enderio.regilite.events;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteMenu;
import com.enderio.regilite.registry.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ScreenEvents {

    private final Regilite regilite;

    public ScreenEvents(Regilite regilite) {
        this.regilite = regilite;
    }

    public <T extends AbstractContainerMenu> void genericScreenEvent(RegisterMenuScreensEvent event) {
        for (DeferredHolder<MenuType<?>, ? extends MenuType<?>> menu : regilite.getMenus()) {
            if (menu instanceof RegiliteMenu regiliteMenu) {
                IScreenConstructor<T, ? extends AbstractContainerScreen<T>> screen = regiliteMenu.getScreenConstructor();

                if (screen != null) {
                    event.register((MenuType<T>)regiliteMenu.get(), screen::create);
                }
            }
        }
    }

    public void screenEvent(RegisterMenuScreensEvent event) {
        this.genericScreenEvent(event);
    }
}
