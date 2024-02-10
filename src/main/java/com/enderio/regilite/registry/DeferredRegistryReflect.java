package com.enderio.regilite.registry;

import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Supplier;

// TODO: This class needs much more robust error handling.
public class DeferredRegistryReflect {
    private static Field entriesField;

    static {
        try {
            entriesField = ObfuscationReflectionHelper.findField(DeferredRegister.class, "entries");
        } catch (Exception e) {
            // TODO: Log a message
            throw e;
        }
    }

    public static <T> Map<DeferredHolder<T, ?>, Supplier<? extends T>> getEntries(DeferredRegister<T> register) {
        try {
            //noinspection unchecked
            return (Map<DeferredHolder<T, ?>, Supplier<? extends T>>)entriesField.get(register);
        } catch (Exception ex) {
            // TODO: log it!
            ex.printStackTrace();
            return null;
        }
    }
}
