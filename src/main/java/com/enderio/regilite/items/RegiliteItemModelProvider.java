/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.items;

import com.enderio.regilite.data.DataGenContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion;

import java.util.Objects;

public class RegiliteItemModelProvider extends ItemModelProvider {
    private final RegiliteItems items;

    public RegiliteItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper, RegiliteItems items) {
        super(output, modid, existingFileHelper);
        this.items = items;
    }

    @Override
    protected void registerModels() {
        items.itemBuilders().forEach(this::registerItemModel);
    }

    private <T extends Item> void registerItemModel(ItemBuilder<T> itemBuilder) {
        var modelProvider = itemBuilder.modelProvider;
        if (modelProvider != null) {
            modelProvider.accept(this, new DataGenContext<>(itemBuilder.getId(), itemBuilder::get));
        }
    }

    // TODO: These could do a better job by somehow getting the block model and ensuring it's right,
    //       eg. https://github.com/tterrag1098/Registrate/blob/1.20/src/main/java/com/tterrag/registrate/builders/BlockBuilder.java#L208

    public ItemModelBuilder basicBlock(Item item) {
        return basicBlock(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder basicBlock(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(
                        item.getNamespace(), "block/" + item.getPath())));
    }

    public ItemModelBuilder basicItem(Item item, ResourceLocation texture) {
        return getBuilder(BuiltInRegistries.ITEM.getKey(item).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", texture);
    }

    public ItemModelBuilder handheld(Item item) {
        return handheld(item, itemTexture(item));
    }

    public ItemModelBuilder handheld(Item item, ResourceLocation texture) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item).toString(), "item/handheld")
                .texture("layer0", texture);
    }

    public ResourceLocation itemTexture(ItemLike item) {
        var itemLocation = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item.asItem()));
        return ResourceLocation.fromNamespaceAndPath(itemLocation.getNamespace(), "item/" + itemLocation.getPath());
    }

    public ItemModelBuilder bucketItem(BucketItem item) {
        return withExistingParent(BuiltInRegistries.ITEM.getKey(item).toString(), ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "item/bucket"))
                .customLoader(DynamicFluidContainerModelBuilder::begin)
                .fluid(item.content)
                .end();
    }
}

