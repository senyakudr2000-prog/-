package com.example.realwater;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(RealWaterMod.MODID)
public class RealWaterMod {
    public static final String MODID = "realwater";

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, MODID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Свойства жидкости: быстрее и дальше растекается, чем ваниль.
    private static BaseFlowingFluid.Properties props() {
        return new BaseFlowingFluid.Properties(REAL_WATER_TYPE, REAL_WATER, REAL_WATER_FLOWING)
                .block(REAL_WATER_BLOCK)
                .slopeFindDistance(6)
                .levelDecreasePerBlock(1)
                .tickRate(3)
                .explosionResistance(100f);
    }

    public static final Supplier<FluidType> REAL_WATER_TYPE = FLUID_TYPES.register("real_water",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.realwater.real_water")
                    .density(1000)
                    .viscosity(1000)
                    .canSwim(true)
                    .canDrown(true)
                    .canExtinguish(true)
                    .canHydrate(true)
                    .canPushEntity(true)
                    .supportsBoating(true)
                    .fallDistanceModifier(0f)
                    .canConvertToSource(true)));

    public static final Supplier<FlowingFluid> REAL_WATER =
            FLUIDS.register("real_water", () -> new BaseFlowingFluid.Source(props()));
    public static final Supplier<FlowingFluid> REAL_WATER_FLOWING =
            FLUIDS.register("real_water_flowing", () -> new BaseFlowingFluid.Flowing(props()));

    public static final Supplier<LiquidBlock> REAL_WATER_BLOCK = BLOCKS.register("real_water",
            () -> new LiquidBlock(REAL_WATER.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final DeferredBlock<WaterEmitterBlock> WATER_EMITTER = BLOCKS.register("water_emitter",
            () -> new WaterEmitterBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.0f, 6.0f)
                    .sound(SoundType.METAL)));

    public static final DeferredItem<BlockItem> WATER_EMITTER_ITEM =
            ITEMS.registerSimpleBlockItem("water_emitter", WATER_EMITTER);

    public RealWaterMod(IEventBus modBus) {
        FLUID_TYPES.register(modBus);
        FLUIDS.register(modBus);
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        modBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(WATER_EMITTER_ITEM);
        }
    }
}
