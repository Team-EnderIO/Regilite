package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleMenu;
import com.enderio.regilite.examplemod.exampleclasses.ExampleScreen;
import com.enderio.regilite.holder.RegiliteMenu;
import com.enderio.regilite.registry.MenuRegistry;
import net.neoforged.bus.api.IEventBus;

public class Menus {

    private static final MenuRegistry MENUS = MenuRegistry.createRegistry(ExampleMod.getRegilite());

    public static final RegiliteMenu<ExampleMenu> EXAMPLE_MENU = MENUS.registerMenu("example", ExampleMenu::new, () -> ExampleScreen::new);

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
    }
}
