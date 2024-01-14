package com.example.examplemod.exampleclasses;

import com.example.examplemod.Menus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExampleMenu extends AbstractContainerMenu {
    public ExampleMenu(int windowId, Inventory inv, FriendlyByteBuf data) {
        super(Menus.EXAMPLE_MENU.get(), windowId);
    }

    public ExampleMenu(ExampleBlockentity be, int windowId, Inventory inv) {
        super(Menus.EXAMPLE_MENU.get(), windowId);
    }

    @Override
    public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return true;
    }
}
