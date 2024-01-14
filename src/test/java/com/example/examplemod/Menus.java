package com.example.examplemod;

import com.example.examplemod.exampleclasses.ExampleMenu;
import com.example.examplemod.exampleclasses.ExampleScreen;
import com.example.regilite.holder.RegiliteMenu;
import com.example.regilite.registry.MenuRegistry;
import net.neoforged.bus.api.IEventBus;

public class Menus {

    private static final MenuRegistry MENUS = MenuRegistry.createRegistry(ExampleMod.MODID);

    public static final RegiliteMenu<ExampleMenu> EXAMPLE_MENU = MENUS.registerMenu("example", ExampleMenu::new, ExampleScreen::new);

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
    }
}
