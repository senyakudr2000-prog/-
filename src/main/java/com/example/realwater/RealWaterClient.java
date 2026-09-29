package com.example.realwater;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

/**
 * Никаких кастомных шейдеров: только текстуры, частицы и ванильный translucent-слой.
 * Это самый безопасный вариант для LTW на телефоне.
 */
@EventBusSubscriber(modid = RealWaterMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RealWaterClient {
    private static final ResourceLocation STILL =
            ResourceLocation.fromNamespaceAndPath(RealWaterMod.MODID, "block/real_water_still");
    private static final ResourceLocation FLOWING =
            ResourceLocation.fromNamespaceAndPath(RealWaterMod.MODID, "block/real_water_flow");
    private static final ResourceLocation OVERLAY =
            ResourceLocation.withDefaultNamespace("block/water_overlay");
    private static final ResourceLocation UNDERWATER =
            ResourceLocation.withDefaultNamespace("textures/misc/underwater.png");

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(RealWaterMod.REAL_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(RealWaterMod.REAL_WATER_FLOWING.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void onClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new IClientFluidTypeExtensions() {
            @Override public ResourceLocation getStillTexture() { return STILL; }
            @Override public ResourceLocation getFlowingTexture() { return FLOWING; }
            @Override public ResourceLocation getOverlayTexture() { return OVERLAY; }
            @Override public ResourceLocation getRenderOverlayTexture(Minecraft mc) { return UNDERWATER; }
            @Override public int getTintColor() { return 0xFFFFFFFF; }
        }, RealWaterMod.REAL_WATER_TYPE.get());
    }
}
