package com.example.examplemod.exampleclasses;

import com.example.examplemod.Blocks;
import com.example.regilite.events.IBlockColor;
import com.example.regilite.events.IItemColor;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;

import java.util.function.Supplier;

public class ExampleColors {

    public static IBlockColor BLOCK = (state, getter, pos, layer) -> 0;

    public static IItemColor ITEM = (stack, layer) -> 0;

}
