package com.enderio.regilite.menus;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class RegiliteClientMenuTypes {
    private final RegiliteMenuTypes regiliteMenuTypes;

    public RegiliteClientMenuTypes(RegiliteMenuTypes regiliteMenuTypes) {
        this.regiliteMenuTypes = regiliteMenuTypes;
    }

    @SubscribeEvent
    public void registerScreens(RegisterMenuScreensEvent event) {
        for (var menu : regiliteMenuTypes.menus) {
            registerScreen(event, menu);
        }
    }

    private <T extends AbstractContainerMenu> void registerScreen(RegisterMenuScreensEvent event, MenuTypeBuilder<T> builder) {
        if (builder.screenConstructor != null) {
            event.register(builder.get(), builder.screenConstructor.get()::create);
        }
    }
}
