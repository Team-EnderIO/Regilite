package com.enderio.regilite.data;

import java.util.function.Supplier;

public class DataGenContext<R, E extends R> implements Supplier<E> {
    private Supplier<E> entry;
    private String name;

    public DataGenContext(String name, Supplier<E> entry) {
        this.name = name;
        this.entry = entry;
    }

    @Override
    public E get() {
        return entry.get();
    }

    public String getName() {
        return name;
    }
}
