package com.example.regilite.events;

import net.minecraft.world.item.ItemStack;

public interface IItemColor {
    int getColor(ItemStack stack, int layer);
}
