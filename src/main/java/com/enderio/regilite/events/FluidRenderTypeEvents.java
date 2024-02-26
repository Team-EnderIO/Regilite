package com.enderio.regilite.events;

import com.enderio.regilite.holder.RegiliteFluid;
import com.enderio.regilite.registry.FluidRegister;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.fluids.FluidType;

public class FluidRenderTypeEvents {

    public FluidRenderTypeEvents() {
    }

    public void registerRenderTypes(FMLClientSetupEvent clientSetupEvent) {
        for (var fluid : FluidRegister.getRegistered()) {
            if (fluid instanceof RegiliteFluid<? extends FluidType> regiliteFluid) {
                var renderType = regiliteFluid.getRenderType().get();
                if (renderType != null) {
                    ItemBlockRenderTypes.setRenderLayer(regiliteFluid.getSource(), renderType);
                    ItemBlockRenderTypes.setRenderLayer(regiliteFluid.getFlowing(), renderType);
                }
            }
        }
    }
}
