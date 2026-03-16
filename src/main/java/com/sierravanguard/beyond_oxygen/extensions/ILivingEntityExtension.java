package com.sierravanguard.beyond_oxygen.extensions;

import net.minecraftforge.event.entity.living.LivingBreatheEvent;

public interface ILivingEntityExtension extends IEntityExtension {
    void beyond_oxygen$tick();

    void beyond_oxygen$breathe(LivingBreatheEvent event);
}
