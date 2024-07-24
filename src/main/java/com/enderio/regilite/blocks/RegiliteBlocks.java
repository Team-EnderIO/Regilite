/*
 * Copyright (c) Team Ender IO and contributors
 * SPDX-License-Identifier: LGPL-3.0-only
 */

package com.enderio.regilite.blocks;

import com.enderio.regilite.Regilite;
import com.enderio.regilite.RegiliteModuleEvents;
import com.enderio.regilite.RegiliteRegistryModule;
import com.enderio.regilite.items.RegiliteItems;
import com.enderio.regilite.lang.RegiliteLang;
import com.enderio.regilite.loot.RegiliteLootTables;
import com.enderio.regilite.tags.RegiliteTags;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class RegiliteBlocks implements RegiliteRegistryModule<Block, DeferredRegister.Blocks>, RegiliteModuleEvents {
    private final RegiliteLang langModule;
    private final RegiliteTags tagsModule;
    private final RegiliteItems itemsModule;
    private final RegiliteLootTables lootTablesModule;

    private final DeferredRegister.Blocks deferredRegister;

    // Tracks all of the builders so that they can be used for data-generation.
    private final ObjectList<BlockBuilder<? extends Block>> blocks = new ObjectArrayList<>();

    public RegiliteBlocks(RegiliteLang langModule, RegiliteTags tagsModule, RegiliteItems itemsModule, RegiliteLootTables lootTablesModule, DeferredRegister.Blocks deferredRegister) {
        this.langModule = langModule;
        this.tagsModule = tagsModule;
        this.itemsModule = itemsModule;
        this.lootTablesModule = lootTablesModule;
        this.deferredRegister = deferredRegister;
    }

    @ApiStatus.Internal
    public Stream<BlockBuilder<? extends Block>> blockBuilders() {
        return blocks.stream();
    }

    public <B extends Block> BlockBuilder<B> create(String name, Supplier<? extends B> supplier) {
        DeferredBlock<B> holder = deferredRegister.register(name, supplier);
        var builder = new BlockBuilder<>(holder, langModule, tagsModule, itemsModule);
        blocks.add(builder);
        return builder;
    }

    public <B extends Block> BlockBuilder<B> create(String name, Function<BlockBehaviour.Properties, ? extends B> func, BlockBehaviour.Properties props) {
        return create(name, () -> func.apply(props));
    }

    public BlockBuilder<Block> createSimple(String name, BlockBehaviour.Properties props) {
        return create(name, Block::new, props);
    }

    @ApiStatus.Internal
    public static RegiliteBlocks create(Regilite regilite) {
        return new RegiliteBlocks(regilite.lang(), regilite.tags(), regilite.items(), regilite.lootTables(), DeferredRegister.createBlocks(regilite.getModId()));
    }

    @Override
    public ResourceKey<Registry<Block>> registry() {
        return Registries.BLOCK;
    }

    @Override
    public DeferredRegister.Blocks deferredRegister() {
        return deferredRegister;
    }

    @Override
    public void register(IEventBus modEventBus) {
        deferredRegister.register(modEventBus);

        lootTablesModule.addLootTableProvider(() -> new LootTableProvider.SubProviderEntry(
                prov -> new RegiliteBlockLootProvider(prov, this), LootContextParamSets.BLOCK));

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.register(new RegiliteClientBlocks(this));
        }
    }

    // TODO: Create data providers for loot tables, block states etc.
}
