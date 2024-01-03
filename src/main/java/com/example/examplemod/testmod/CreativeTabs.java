package com.example.examplemod.testmod;

import com.example.examplemod.data.EnderDataProvider;
import com.example.examplemod.events.ColorEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExampleMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = registerTab("example_tab", "Example", builder ->
            builder.withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> Items.EXAMPLE_ITEM.get().getDefaultInstance()));

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static DeferredHolder<CreativeModeTab, CreativeModeTab> registerTab(String name, String translation, Consumer<CreativeModeTab.Builder> builder) {
        return CREATIVE_MODE_TABS.register(name, () -> {
            CreativeModeTab.Builder config = CreativeModeTab.builder()
                    .title(EnderDataProvider.addTranslation("config", new ResourceLocation(CREATIVE_MODE_TABS.getNamespace(), name), translation));
            builder.accept(config);
            return config.build();
        });
    }
}
