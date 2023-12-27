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
import java.util.ArrayList;
import java.util.List;

public class EnderLangProvider extends LanguageProvider {
    private final List<DeferredHolder<Block, ? extends Block>> blocks = new ArrayList<>();
    private final List<DeferredHolder<Item, ? extends Item>> items = new ArrayList<>();

    public EnderLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    public void addBlocks(List<DeferredHolder<Block, ? extends Block>> blocks) {
        this.blocks.addAll(blocks);
    }

    public void addItems(List<DeferredHolder<Item, ? extends Item>> items) {
        this.items.addAll(items);
    }

    @Override
    protected void addTranslations() {
        String translation = "";
        for (DeferredHolder<Block, ? extends Block> block : blocks) {
            translation = ((EnderDeferredBlock<Block>) block).getTranslation();
            if (!translation.isEmpty())
                this.add(block.get(), translation);
        }
        for (DeferredHolder<Item, ? extends Item> item : items) {
            translation = ((EnderDeferredItem<Item>) item).getTranslation();
            if (!translation.isEmpty())
                this.add(item.get(), translation);
        }
    }
}
