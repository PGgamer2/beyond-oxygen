package com.sierravanguard.beyond_oxygen.mixin;

import com.sierravanguard.beyond_oxygen.compat.CompatLoader;
import com.sierravanguard.beyond_oxygen.compat.CompatUtils;
import com.sierravanguard.beyond_oxygen.extensions.ILivingEntityExtension;
import com.sierravanguard.beyond_oxygen.registry.BODamageSources;
import com.sierravanguard.beyond_oxygen.registry.BODimensions;
import com.sierravanguard.beyond_oxygen.registry.BOEffects;
import com.sierravanguard.beyond_oxygen.tags.BOEntityTypeTags;
import com.sierravanguard.beyond_oxygen.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntityExtension {
    @Unique
    private LivingEntity beyond_oxygen$self() {
        return (LivingEntity) (Object) this;
    }

    @Unique
    private int beyond_oxygen$vacuumDamageCooldown = 0;

    private LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void beyond_oxygen$tick() {
        if (level().isClientSide()) return;
        LivingEntity self = beyond_oxygen$self();
        OxygenManager.consumeOxygen(self);
        if (beyond_oxygen$vacuumDamageCooldown > 0) {
            beyond_oxygen$vacuumDamageCooldown--;
        }
        if (!self.hasEffect(BOEffects.OXYGEN_SATURATION.get())) {
            if (BODimensions.isUnbreathable(level())
                    && !self.getType().is(BOEntityTypeTags.SURVIVES_VACUUM)
                    && !OxygenHelper.isInBreathableEnvironment(self)
                    && !self.isUnderWater()) {
                if (beyond_oxygen$vacuumDamageCooldown <= 0) {
                    applyDamageWithMessage(self, BODamageSources.vacuum(), 5f);
                    beyond_oxygen$vacuumDamageCooldown = 20;
                }
                beyond_oxygen$vacuumDamageCooldown--;
            }
        }

        if (!CompatLoader.COLD_SWEAT.isLoaded()) {
            boolean blockedByThermalController = beyond_oxygen$getAreasIn().stream().anyMatch(HermeticArea::hasActiveTemperatureRegulator);
            if (!blockedByThermalController) {
                if (BODimensions.isHot(level())
                        && !self.getType().is(BOEntityTypeTags.SURVIVES_HOT)
                        && !SpaceSuitHandler.isWearingFullThermalSuit(self)) {
                    applyDamageWithMessage(self, BODamageSources.burn(), 5f);
                }
                if (BODimensions.isCold(level())
                        && !self.getType().is(BOEntityTypeTags.SURVIVES_COLD)
                        && !SpaceSuitHandler.isWearingFullCryoSuit(self)) {
                    applyDamageWithMessage(self, BODamageSources.freeze(), 5f);
                }
            }
        }

        if (self.hasEffect(BOEffects.OXYGEN_SATURATION.get()) && !self.canDrownInFluidType(Fluids.EMPTY.getFluidType())) {
            self.setAirSupply(self.getMaxAirSupply());
        }
    }

    private static void applyDamageWithMessage(LivingEntity player, DamageSource source, float amount) {
        if (player.level().isClientSide()) return;
        BODamageSources.applyCustomDamage(player, source, amount);
        DamageType type = source.getMsgId() != null ? source.type() : null;
        if (type != null) {
            player.getCombatTracker().recordDamage(source, amount);
        }
    }

    @Override
    public void beyond_oxygen$breathe(LivingBreatheEvent event) {
        LivingEntity self = beyond_oxygen$self();
        FluidType fluidtype = CompatUtils.getEyeFluidType(self);

        boolean isAir = fluidtype.isAir() || self.level().getBlockState(BlockPos.containing(self.getX(), self.getEyeY(), self.getZ())).is(Blocks.BUBBLE_COLUMN)
                || beyond_oxygen$getAreasIn().stream().anyMatch(HermeticArea::hasAir) || self.hasEffect(BOEffects.OXYGEN_SATURATION.get());
        boolean canBreathe = !self.canDrownInFluidType(fluidtype) || MobEffectUtil.hasWaterBreathing(self) || (self instanceof Player && ((Player)self).getAbilities().invulnerable);

        event.setCanBreathe(canBreathe || isAir);
        event.setCanRefillAir(isAir);
    }
}


