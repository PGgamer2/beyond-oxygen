package com.sierravanguard.beyond_oxygen.data;

import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.tags.BODimensionTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BODimensionTagsProvider extends DataRegistryTagsProvider<Level> {
    public BODimensionTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.DIMENSION, lookupProvider, BeyondOxygen.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {
        this.tag(BODimensionTags.UNBREATHABLE)
                .addOptional(ResourceLocation.tryBuild("cosmos", "solar_system"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "mercury_wasteland"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "venuslands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "earth_moon"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "marslands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "jupiterlands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "europa_lands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "saturn_lands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "uranus_lands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "neptune_lands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "plutowastelands"))

                .addOptional(ResourceLocation.tryBuild("cosmos", "alpha_system"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "b_1400_centauri"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "gaia_bh_1"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "glacio_lands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "j_1407blands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "j_1900"));

        this.tag(BODimensionTags.COLD)
                .addOptional(ResourceLocation.tryBuild("cosmos", "plutowastelands"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "glacio_lands"));

        this.tag(BODimensionTags.HOT)
                .addOptional(ResourceLocation.tryBuild("cosmos", "mercury_wasteland"))
                .addOptional(ResourceLocation.tryBuild("cosmos", "venuslands"));

    }
}
