package com.example.regilite.events;

import com.example.regilite.holder.RegiliteBlock;
import com.example.regilite.holder.RegiliteItem;
import com.example.regilite.registry.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ColorEvents {

    public static class Items {
        private final ItemRegistry items;

        public Items(ItemRegistry items) {
            this.items = items;
        }

        public void registerItemColor(RegisterColorHandlersEvent.Item event) {
            for (DeferredHolder<Item, ? extends Item> item : items.getEntries()) {
                if (item instanceof RegiliteItem) {
                    IItemColor colorSupplier = ((RegiliteItem<Item>) item).getColorSupplier();
                    if (colorSupplier != null)
                        event.register(colorSupplier::getColor, item.get());
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
                    IBlockColor colorSupplier = ((RegiliteBlock<Block>) block).getColorSupplier();
                    if (colorSupplier != null)
                        event.register(colorSupplier::getColor, block.get());
                }
            }
        }
    }
}
