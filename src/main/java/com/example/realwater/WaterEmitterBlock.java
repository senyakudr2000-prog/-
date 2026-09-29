package com.example.realwater;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Пока блок стоит, сверху него постоянно поддерживается ИСТОЧНИК воды.
 * Источник - обычный блок жидкости, поэтому после слома эмиттера вся вылитая вода остаётся.
 */
public class WaterEmitterBlock extends Block {
    private static final int CHECK_INTERVAL = 5;

    public WaterEmitterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos up = pos.above();
        BlockState target = level.getBlockState(up);

        boolean alreadySource = target.is(RealWaterMod.REAL_WATER_BLOCK.get())
                && target.getValue(LiquidBlock.LEVEL) == 0;

        if (!alreadySource && (target.isAir() || target.canBeReplaced(RealWaterMod.REAL_WATER.get()))) {
            level.setBlock(up, RealWaterMod.REAL_WATER_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
        }
        level.scheduleTick(pos, this, CHECK_INTERVAL);
    }

    // Только клиент: брызги и звук журчания.
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.05;
        double z = pos.getZ() + 0.5;
        for (int i = 0; i < 2; i++) {
            level.addParticle(ParticleTypes.SPLASH,
                    x + (random.nextDouble() - 0.5) * 0.5, y, z + (random.nextDouble() - 0.5) * 0.5,
                    0.0, 0.05 + random.nextDouble() * 0.05, 0.0);
        }
        if (random.nextInt(30) == 0) {
            level.playLocalSound(x, y, z, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS,
                    0.5f, 0.9f + random.nextFloat() * 0.2f, false);
        }
    }
}
