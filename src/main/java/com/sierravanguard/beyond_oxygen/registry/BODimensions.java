package com.sierravanguard.beyond_oxygen.registry;

import com.sierravanguard.beyond_oxygen.BOConfig;
import com.sierravanguard.beyond_oxygen.BeyondOxygen;
import com.sierravanguard.beyond_oxygen.tags.BODimensionTags;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.*;

public class BODimensions {
    private static List<ResourceLocation> unbreathableTag = List.of();
    private static List<ResourceLocation> coldTag = List.of();
    private static List<ResourceLocation> hotTag = List.of();

    private static Set<ResourceLocation> unbreathable = Set.of();
    private static Set<ResourceLocation> cold = Set.of();
    private static Set<ResourceLocation> hot = Set.of();

    public static void populateDimensions(RegistryAccess registryAccess) {
        HolderLookup.RegistryLookup<Level> lookup = registryAccess.lookupOrThrow(Registries.DIMENSION);
        unbreathableTag = lookup.get(BODimensionTags.UNBREATHABLE).map(BODimensions::toList).orElseGet(List::of);
        coldTag = lookup.get(BODimensionTags.COLD).map(BODimensions::toList).orElseGet(List::of);
        hotTag = lookup.get(BODimensionTags.HOT).map(BODimensions::toList).orElseGet(List::of);
        updateSets();
    }

    private static List<ResourceLocation> toList(HolderSet<Level> holderSet) {
        return holderSet.stream().map(Holder::unwrapKey).filter(Optional::isPresent).map(Optional::get).map(ResourceKey::location).toList();
    }

    public static void releaseDimensions() {
        unbreathableTag = List.of();
        coldTag = List.of();
        hotTag = List.of();
        updateSets();
    }

    public static void updateSets() {
        unbreathable = combine(unbreathableTag, BOConfig.unbreathableDimensionsWhitelist(), BOConfig.unbreathableDimensionsBlacklist());
        printDebug("Unbreathable dimensions: ", unbreathable);
        cold = combine(coldTag, BOConfig.coldDimensionsWhitelist(), BOConfig.coldDimensionsBlacklist());
        printDebug("Cold dimensions: ", cold);
        hot = combine(hotTag, BOConfig.hotDimensionsWhitelist(), BOConfig.hotDimensionsBlacklist());
        printDebug("Hot dimensions: ", hot);
    }

    private static Set<ResourceLocation> combine(List<ResourceLocation> tagList, List<ResourceLocation> whitelist, List<ResourceLocation> blacklist) {
        Set<ResourceLocation> set = new HashSet<>(tagList);
        set.addAll(whitelist);
        blacklist.forEach(set::remove);
        return set;
    }

    private static void printDebug(String prefix, Set<ResourceLocation> set) {
        StringJoiner joiner = new StringJoiner(", ", prefix, "");
        for (ResourceLocation id : set) joiner.add(id.toString());
        BeyondOxygen.LOGGER.debug(joiner.toString());
    }

    public static boolean isUnbreathable(Level level) {
        return BOConfig.enableUnbreathableDimensions() && unbreathable.contains(level.dimension().location());
    }

    public static boolean isCold(Level level) {
        return BOConfig.enableColdDimensions() && cold.contains(level.dimension().location());
    }

    public static boolean isHot(Level level) {
        return BOConfig.enableHotDimensions() && hot.contains(level.dimension().location());
    }
}