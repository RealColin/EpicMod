package github.realcolin.epicmod.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import github.realcolin.epicmod.util.ImageWrapper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class EpicBiomeSource extends BiomeSource {

    private static final MapCodec<Integer> COLOR =
            RecordCodecBuilder.mapCodec(yes -> yes.group(
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("color").forGetter(a -> a)
            ).apply(yes, Integer::valueOf));

    private static final Codec<List<Pair<Holder<Biome>, Integer>>> BIOME_COLOR_CODEC =
            RecordCodecBuilder.<Pair<Holder<Biome>, Integer>>create(yes -> yes.group(
                    Biome.CODEC.fieldOf("biome").forGetter(Pair::getFirst),
                    COLOR.forGetter(Pair::getSecond)
            ).apply(yes, Pair::of)).listOf();

    public static final MapCodec<EpicBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(yea -> yea.group(
                    Biome.CODEC.fieldOf("default").forGetter(src -> src._default),
                    BIOME_COLOR_CODEC.fieldOf("biomes").forGetter(src -> src.biomes)
            ).apply(yea, EpicBiomeSource::new));

    private final Holder<Biome> _default;
    private final List<Pair<Holder<Biome>, Integer>> biomes;
    private final ImageWrapper biomeMap;
    private final Random random = new Random();

    public EpicBiomeSource(Holder<Biome> _default, List<Pair<Holder<Biome>, Integer>> biomes) {
        this._default = _default;
        this.biomes = biomes;
        this.biomeMap = new ImageWrapper("biomes");
    }

    @Override
    protected @NotNull MapCodec<EpicBiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull Stream<Holder<Biome>> collectPossibleBiomes() {
        return biomes.stream().map(Pair::getFirst);
    }


    // works decent enough for now, i wanna look at how other mods do this in order to see how they fix the
    // problem of having jarring biome edges, especially at higher scales
    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.@NotNull Sampler sampler) {
        /*
         * By how much we divide the input coordinates before using them as pixel coordinates
         * The input coordinates are a 4x4 section of blocks, so a scale of 4 would be a 16x16 section of blocks,
         * a scale of 8 would be a 32x32 section of blocks, and so on
         */
        int scale = 8;

        /*
         * First, get jitter values
         */

        // TODO implement this WITHOUT random
        int jx = random.nextInt(0, scale + 1) - (scale / 2);
        int jz = random.nextInt(0, scale + 1) - (scale / 2);

        /*
         * Next, get the color at the pixel position offset by the jitter
         */
        int px = (x + jx) / scale, pz = (z + jz) / scale;
        int jitteredColor = biomeMap.getColorAtPixel(px, pz);

        /*
         * Then, make sure at least one neighbor of the current pixel has a matching color to jitteredColor
         * if it does, then jitteredColor will be used to pick the biome at this position
         * if it does not, then the correct pixel color will be used to pick the biome at this position
         */
        px = x / scale;
        pz = z / scale;
        int color;

        if (biomeMap.getColorAtPixel(px, pz) == jitteredColor) {
            color = jitteredColor;
        } else {
            if (biomeMap.getColorAtPixel(px + 1, pz) == jitteredColor) {
                color = jitteredColor;
            } else if (biomeMap.getColorAtPixel(px - 1, pz) == jitteredColor) {
                color = jitteredColor;
            } else if (biomeMap.getColorAtPixel(px, pz + 1) == jitteredColor) {
                color = jitteredColor;
            } else if (biomeMap.getColorAtPixel(px, pz - 1) == jitteredColor) {
                color = jitteredColor;
            } else {
                color = biomeMap.getColorAtPixel(px, pz);
            }
        }

        if (color != -1) {
            for (var pair : biomes) {
                if (pair.getSecond() == color)
                    return pair.getFirst();
            }
        }
        return this._default;
    }

    public static void registerBiomeSource() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(EpicMod.MOD_ID, "epic_source"), CODEC);
    }
}
