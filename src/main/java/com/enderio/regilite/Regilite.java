package com.enderio.regilite;

import com.enderio.regilite.data.RegiliteDataProvider;
import com.enderio.regilite.events.BlockEntityCapabilityEvents;
import com.enderio.regilite.events.BlockEntityRendererEvents;
import com.enderio.regilite.events.ColorEvents;
import com.enderio.regilite.events.EntityRendererEvents;
import com.enderio.regilite.events.FluidRenderTypeEvents;
import com.enderio.regilite.events.ItemCapabilityEvents;
import com.enderio.regilite.events.ScreenEvents;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.BlockEntityRegistry;
import com.enderio.regilite.registry.BlockRegistry;
import com.enderio.regilite.registry.EntityRegistry;
import com.enderio.regilite.registry.FluidRegistry;
import com.enderio.regilite.registry.ItemRegistry;
import com.enderio.regilite.registry.MenuRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Regilite {

    private final String modid;

    private final List<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> blockentities = new ArrayList<>();
    private final List<DeferredHolder<Block, ? extends Block>> blocks = new ArrayList<>();
    private final List<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entities = new ArrayList<>();
    private final List<DeferredHolder<FluidType, ? extends FluidType>> fluids = new ArrayList<>();
    private final List<DeferredHolder<Item, ? extends Item>> items = new ArrayList<>();
    private final List<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> menus = new ArrayList<>();
    private final RegiliteDataProvider dataProvider;


    public Regilite(String modid) {
        this.modid = modid;
        this.dataProvider = new RegiliteDataProvider(this);
    }

    public void register(IEventBus modbus) {
        dataProvider.register(modbus);

        if (FMLEnvironment.dist.isClient()) {
            modbus.addListener(new ColorEvents.Blocks(this)::registerBlockColor);

            modbus.addListener(new ColorEvents.Items(this)::registerItemColor);
            modbus.addListener(new ItemCapabilityEvents(this)::registerCapabilities);
            modbus.addListener(this::addCreative);

            modbus.addListener(new BlockEntityRendererEvents(this)::registerBER);
            modbus.addListener(new BlockEntityCapabilityEvents(this)::registerCapabilities);

            modbus.addListener(new FluidRenderTypeEvents(this)::registerRenderTypes);

            modbus.addListener(new EntityRendererEvents(this)::registerER);

            modbus.addListener(new ScreenEvents(this)::screenEvent);
        }
    }

    public String getModid() {
        return modid;
    }

    public BlockRegistry blockRegistry() {
        return BlockRegistry.create(this);
    }

    public BlockEntityRegistry blockEntityRegistry() {
        return BlockEntityRegistry.create(this);
    }

    public EntityRegistry entityRegistry() {
        return EntityRegistry.create(this);
    }

    public FluidRegistry fluidRegistry() {
        return FluidRegistry.create(this);
    }

    public ItemRegistry itemRegistry() {
        return ItemRegistry.create(this);
    }

    public MenuRegistry menuRegistry() {
        return MenuRegistry.create(this);
    }

    public List<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> getBlockEntities() {
        return blockentities;
    }

    public void addBlockEntities(Collection<DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>>> entries) {
        this.blockentities.addAll(entries);
    }

    public List<DeferredHolder<Block, ? extends Block>> getBlock() {
        return blocks;
    }

    public void addBlocks(Collection<DeferredHolder<Block, ? extends Block>> entries) {
        this.blocks.addAll(entries);
    }

    public List<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> getEntities() {
        return entities;
    }

    public void addEntities(Collection<DeferredHolder<EntityType<?>, ? extends EntityType<?>>> entries) {
        this.entities.addAll(entries);
    }

    public List<DeferredHolder<FluidType, ? extends FluidType>> getFluids() {
        return fluids;
    }

    public void addFluids(Collection<DeferredHolder<FluidType, ? extends FluidType>> entries) {
        this.fluids.addAll(entries);
    }

    public List<DeferredHolder<Item, ? extends Item>> getItems() {
        return items;
    }

    public void addItems(Collection<DeferredHolder<Item, ? extends Item>> entries) {
        this.items.addAll(entries);
    }

    public List<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> getMenus() {
        return menus;
    }

    public void addMenus(Collection<DeferredHolder<MenuType<?>, ? extends MenuType<?>>> entries) {
        this.menus.addAll(entries);
    }

    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        for (DeferredHolder<Item, ? extends Item> item : getItems()) {
            if (item instanceof RegiliteItem) {
                Consumer<CreativeModeTab.Output> outputConsumer = ((RegiliteItem<Item>) item).getTab().get(event.getTabKey());
                if (outputConsumer != null) {
                    outputConsumer.accept(event);
                }
            }
        }
    }

    public MutableComponent addTranslation(String prefix, ResourceLocation location, String translation) {
        return dataProvider.addTranslation(prefix + "." + location.toLanguageKey(), translation);
    }

    public void addTranslation(Supplier<String> key, String translation) {
        dataProvider.addTranslation(key, translation);
    }
}
