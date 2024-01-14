package com.example.examplemod;

import com.example.examplemod.exampleclasses.ExampleMenu;
import com.example.examplemod.exampleclasses.ExampleScreen;
import com.example.regilite.registry.EnderDeferredMenu;
import com.example.regilite.registry.EnderMenuRegistry;
import net.neoforged.bus.api.IEventBus;

public class Menus {

    private static final EnderMenuRegistry MENUS = EnderMenuRegistry.createRegistry(ExampleMod.MODID);

    public static final EnderDeferredMenu<ExampleMenu> EXAMPLE_MENU = MENUS.registerMenu("example", ExampleMenu::new, ExampleScreen::new);

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
    }
}
