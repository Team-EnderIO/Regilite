package com.example.examplemod.events;

import com.example.examplemod.registry.EnderBlockRegistry;
import com.example.examplemod.registry.EnderDeferredBlock;
import com.example.examplemod.registry.EnderDeferredItem;
import com.example.examplemod.registry.EnderItemRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ColorEvents {

    @Nullable
    private final EnderBlockRegistry blocks;
    @Nullable
    private final EnderItemRegistry items;

    public ColorEvents(@Nullable EnderBlockRegistry blocks, @Nullable EnderItemRegistry items) {
        this.blocks = blocks;
        this.items = items;
    }

    public void registerBlockColor(RegisterColorHandlersEvent.Block event) {
        if (blocks == null) {
            return;
        }
        for (DeferredHolder<Block, ? extends Block> block : blocks.getEntries()) {
            Supplier<BlockColor> colorSupplier = ((EnderDeferredBlock<Block>) block).getColorSupplier();
            if (colorSupplier != null)
                event.register(colorSupplier.get(), block.get());
        }
    }

    public void registerItemColor(RegisterColorHandlersEvent.Item event) {
        if (items == null) {
            return;
        }
        for (DeferredHolder<Item, ? extends Item> item : items.getEntries()) {
            Supplier<ItemColor> colorSupplier = ((EnderDeferredItem<Item>) item).getColorSupplier();
            if (colorSupplier != null)
                event.register(colorSupplier.get(), item.get());
        }
    }
}
