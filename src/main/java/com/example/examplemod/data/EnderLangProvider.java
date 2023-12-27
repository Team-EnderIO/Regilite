package com.example.examplemod.data;

import com.example.examplemod.registry.EnderBlockRegistry;
import com.example.examplemod.registry.EnderDeferredBlock;
import com.example.examplemod.registry.EnderDeferredItem;
import com.example.examplemod.registry.EnderItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;

public class EnderLangProvider extends LanguageProvider {
    @Nullable
    private EnderBlockRegistry blocks;
    @Nullable
    private EnderItemRegistry items;

    public EnderLangProvider(PackOutput output, String modid, String locale, EnderBlockRegistry registry) {
        super(output, modid, locale);
        this.blocks = registry;
    }

    public EnderLangProvider(PackOutput output, String modid, String locale, EnderItemRegistry registry) {
        super(output, modid, locale);
        this.items = registry;
    }

    @Override
    protected void addTranslations() {
        if (blocks != null) {
            for (DeferredHolder<Block, ? extends Block> block : blocks.getEntries()) {
                this.add(block.get(), ((EnderDeferredBlock<Block>) block).getTranslation());
            }
        }
        if (items != null) {
            for (DeferredHolder<Item, ? extends Item> item : items.getEntries()) {
                this.add(item.get(), ((EnderDeferredItem<Item>) item).getTranslation());
            }
        }
    }
}
