package github.realcolin.epicmod.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import github.realcolin.epicmod.util.ImageWrapper;
import github.realcolin.epicmod.worldgen.noise.Perlin;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
    private final Perlin perlin = new Perlin(1);

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



    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.@NotNull Sampler sampler) {
        int scale = 4;
        double jitter = perlin.sample((x + 0.5) / 2, (z + 0.5) / 2);
        int rounded = (int)Math.round(jitter * scale);

        int color = biomeMap.getColorAtPixel((x + rounded) / scale, (z + rounded) / scale);

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
