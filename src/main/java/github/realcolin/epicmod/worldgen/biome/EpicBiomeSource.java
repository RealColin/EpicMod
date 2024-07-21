package github.realcolin.epicmod.worldgen.biome;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

public class EpicBiomeSource extends BiomeSource {
    // public static final MapCodec<EpicBiomeSource> CODEC;

    @Override
    protected MapCodec<EpicBiomeSource> codec() {
        return null;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.empty();
    }

    @Override
    public Holder<Biome> getNoiseBiome(int i, int i1, int i2, Climate.Sampler sampler) {
        return null;
    }
}
