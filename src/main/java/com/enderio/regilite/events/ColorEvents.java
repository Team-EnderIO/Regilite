package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ColorEvents {

    public static class Items {
        private final ItemRegistry items;

        public Items(ItemRegistry items) {
            this.items = items;
        }

        public void registerItemColor(RegisterColorHandlersEvent.Item event) {
            for (DeferredHolder<Item, ? extends Item> item : items.getEntries()) {
                if (item instanceof RegiliteItem) {
                    Supplier<ItemColor> colorSupplier = ((RegiliteItem<Item>) item).getColorSupplier();
                    if (colorSupplier != null)
                        event.register(colorSupplier.get(), item.get());
                }
            }
        }
    }

    public static class Blocks {
        private final BlockRegistry blocks;

        public Blocks(BlockRegistry blocks) {
            this.blocks = blocks;
        }

        public void registerBlockColor(RegisterColorHandlersEvent.Block event) {
            for (DeferredHolder<Block, ? extends Block> block : blocks.getEntries()) {
                if (block instanceof RegiliteBlock) {
                    Supplier<BlockColor> colorSupplier = ((RegiliteBlock<Block>) block).getColorSupplier();
                    if (colorSupplier != null)
                        event.register(colorSupplier.get(), block.get());
                }
            }
        }
    }
}
