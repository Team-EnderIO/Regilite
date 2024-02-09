package com.enderio.regilite.events;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
    U create(T p_96215_, Inventory p_96216_, Component p_96217_);
}
