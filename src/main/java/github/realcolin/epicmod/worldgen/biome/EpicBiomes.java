package github.realcolin.epicmod.worldgen.biome;

import com.mojang.serialization.MapCodec;
import github.realcolin.epicmod.EpicMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;

public class EpicBiomes {
    public static final ResourceKey<Biome> WEIRD_BIOME = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(EpicMod.MOD_ID, "weird_biome"));

}
