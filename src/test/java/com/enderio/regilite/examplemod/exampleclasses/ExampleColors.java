package com.enderio.regilite.examplemod.exampleclasses;

import com.enderio.regilite.events.IBlockColor;
import com.enderio.regilite.events.IItemColor;

public class ExampleColors {

    public static IBlockColor BLOCK = (state, getter, pos, layer) -> 0;

    public static IItemColor ITEM = (stack, layer) -> 0;

}
