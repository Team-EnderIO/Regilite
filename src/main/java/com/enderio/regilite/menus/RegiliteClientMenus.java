package com.enderio.regilite.menus;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class RegiliteClientMenus {
    private final RegiliteMenus regiliteMenus;

    public RegiliteClientMenus(RegiliteMenus regiliteMenus) {
        this.regiliteMenus = regiliteMenus;
    }

    @SubscribeEvent
    public void registerScreens(RegisterMenuScreensEvent event) {
        for (var menu : regiliteMenus.menus) {
            registerScreen(event, menu);
        }
    }

    private <T extends AbstractContainerMenu> void registerScreen(RegisterMenuScreensEvent event, MenuBuilder<T> builder) {
        if (builder.screenConstructor != null) {
            event.register(builder.get(), builder.screenConstructor.get()::create);
        }
    }
}
