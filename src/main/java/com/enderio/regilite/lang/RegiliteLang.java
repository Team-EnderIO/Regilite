/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.lang;

import com.enderio.regilite.RegiliteModuleDataGen;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegiliteLang implements RegiliteModuleDataGen {
    private final String modId;
    private final Object2ObjectMap<String, String> basicEntries = new Object2ObjectOpenHashMap<>();

    private final Object2ObjectMap<Supplier<? extends Block>, String> blockEntries = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<Supplier<? extends Item>, String> itemEntries = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<Supplier<? extends MobEffect>, String> mobEffectEntries = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<Supplier<? extends EntityType<?>>, String> entityTypeEntries = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<Supplier<? extends TagKey<?>>, String> tagEntries = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<Supplier<? extends FluidType>, String> fluidTypeEntries = new Object2ObjectOpenHashMap<>();

    public RegiliteLang(String modId) {
        this.modId = modId;
    }

    public MutableComponent add(String prefix, ResourceLocation location, String translation) {
        return add(prefix + "." + location.toLanguageKey(), translation);
    }

    public MutableComponent add(String key, String translation) {
        basicEntries.put(key, translation);
        return Component.translatable(key);
    }

    public void addBlock(Supplier<? extends Block> block, String translation) {
        blockEntries.put(block, translation);
    }

    /**
     * @implNote Must be the same supplier used to add the translation.
     */
    public void removeBlock(Supplier<? extends Block> block) {
        blockEntries.remove(block);
    }

    public void addItem(Supplier<? extends Item> item, String translation) {
        itemEntries.put(item, translation);
    }

    /**
     * @implNote Must be the same supplier used to add the translation.
     */
    public void removeItem(Supplier<? extends Item> item) {
        itemEntries.remove(item);
    }

    public void addMobEffect(Supplier<? extends MobEffect> mobEffect, String translation) {
        mobEffectEntries.put(mobEffect, translation);
    }

    /**
     * @implNote Must be the same supplier used to add the translation.
     */
    public void removeMobEffect(Supplier<? extends MobEffect> mobEffect) {
        mobEffectEntries.remove(mobEffect);
    }

    public void addEntity(Supplier<? extends EntityType<?>> entityType, String translation) {
        entityTypeEntries.put(entityType, translation);
    }

    /**
     * @implNote Must be the same supplier used to add the translation.
     */
    public void removeEntity(Supplier<? extends EntityType<?>> entityType) {
        entityTypeEntries.remove(entityType);
    }

    public void addTag(Supplier<? extends TagKey<?>> tagKey, String translation) {
        tagEntries.put(tagKey, translation);
    }

    /**
     * @implNote Must be the same supplier used to add the translation.
     */
    public void removeTag(Supplier<? extends TagKey<?>> tagKey) {
        tagEntries.remove(tagKey);
    }

    public void addFluid(Supplier<? extends FluidType> fluidType, String translation) {
        fluidTypeEntries.put(fluidType, translation);
    }

    /**
     * @implNote Must be the same supplier used to add the translation.
     */
    public void removeFluid(Supplier<? extends FluidType> fluidType) {
        fluidTypeEntries.remove(fluidType);
    }

    @Override
    public void gatherProviders(GatherDataEvent event, Consumer<DataProvider> addProvider) {
        if (!event.includeClient()) {
            return;
        }

        addProvider.accept(new DefaultProvider(event.getGenerator().getPackOutput(), this.modId, "en_us"));
    }

    private class DefaultProvider extends LanguageProvider {
        public DefaultProvider(PackOutput output, String modid, String locale) {
            super(output, modid, locale);
        }

        @Override
        protected void addTranslations() {
            for (Map.Entry<String, String> entry : basicEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey(), entry.getValue());
                }
            }

            for (Map.Entry<Supplier<? extends Block>, String> entry : blockEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get(), entry.getValue());
                }
            }

            for (Map.Entry<Supplier<? extends Item>, String> entry : itemEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get(), entry.getValue());
                }
            }

            for (Map.Entry<Supplier<? extends MobEffect>, String> entry : mobEffectEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get(), entry.getValue());
                }
            }

            for (Map.Entry<Supplier<? extends EntityType<?>>, String> entry : entityTypeEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get(), entry.getValue());
                }
            }

            for (Map.Entry<Supplier<? extends TagKey<?>>, String> entry : tagEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get(), entry.getValue());
                }
            }

            for (Map.Entry<Supplier<? extends FluidType>, String> entry : fluidTypeEntries.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    this.add(entry.getKey().get().getDescriptionId(), entry.getValue());
                }
            }
        }

        @Override
        public String getName() {
            return "Regilite " + super.getName();
        }
    }
}
