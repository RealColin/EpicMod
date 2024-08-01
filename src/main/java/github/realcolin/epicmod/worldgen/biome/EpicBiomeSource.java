package github.realcolin.epicmod.worldgen.biome;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

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

    public EpicBiomeSource(Holder<Biome> _default, List<Pair<Holder<Biome>, Integer>> biomes) {
        this._default = _default;
        this.biomes = biomes;
    }

    @Override
    protected MapCodec<EpicBiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return biomes.stream().map(Pair::getFirst);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        if (x == 0 && z == 0)
            return this.biomes.getFirst().getFirst();
        
        return this._default;
    }

    public static void registerBiomeSource() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(EpicMod.MOD_ID, "epic_source"), CODEC);
    }
}
