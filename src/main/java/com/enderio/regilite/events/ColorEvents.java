package com.enderio.regilite.events;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.holder.RegiliteBlock;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ColorEvents {

    public static class Items {

        private final Regilite regilite;

        public Items(Regilite regilite) {
            this.regilite = regilite;
        }

        public void registerItemColor(RegisterColorHandlersEvent.Item event) {
            for (DeferredHolder<Item, ? extends Item> item : regilite.getItems()) {
                if (item instanceof RegiliteItem) {
                    var colorSupplier = ((RegiliteItem<Item>) item).getColorSupplier();
                    if (colorSupplier != null)
                        event.register(colorSupplier.get().get(), item.get());
                }
            }
        }
    }

    public static class Blocks {

        private final Regilite regilite;

        public Blocks(Regilite regilite) {
            this.regilite = regilite;
        }

        public void registerBlockColor(RegisterColorHandlersEvent.Block event) {
            for (DeferredHolder<Block, ? extends Block> block : regilite.getBlock()) {
                if (block instanceof RegiliteBlock) {
                    var colorSupplier = ((RegiliteBlock<Block>) block).getColorSupplier();
                    if (colorSupplier != null)
                        event.register(colorSupplier.get().get(), block.get());
                }
            }
        }
    }
}
