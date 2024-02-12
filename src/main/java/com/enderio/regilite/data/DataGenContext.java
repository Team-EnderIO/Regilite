package com.enderio.regilite.data;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class DataGenContext<R, E extends R> implements Supplier<E> {
    private Supplier<E> entry;
    private String modid;
    private String name;

    public DataGenContext(ResourceLocation id, Supplier<E> entry) {
        this.modid = id.getNamespace();
        this.name = id.getPath();
        this.entry = entry;
    }

    @Override
    public E get() {
        return entry.get();
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getId() {
        return new ResourceLocation(modid, name);
    }
}
