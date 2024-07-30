package github.realcolin.epicmod.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

public class EpicBiomeSource extends BiomeSource {

    public static final MapCodec<EpicBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(yea -> yea.group(
                Biome.CODEC.fieldOf("default").forGetter(src -> src._default),
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(src -> src.biomes)
            ).apply(yea, EpicBiomeSource::new));

//    public static final MapCodec<EpicBiomeSource> CODEC =
//            RecordCodecBuilder.mapCodec(yea -> yea.group(
//                    RegistryOps.retrieveElement(EpicBiomes.WEIRD_BIOME),
//                    RegistryOps.retrieveElement(Biomes.PLAINS)
//            ).apply(yea, yea.stable(EpicBiomeSource::new)));

    //private final Holder<Biome> main;
    //private final Holder<Biome> second;

    private final Holder<Biome> _default;
    private final HolderSet<Biome> biomes;

    public EpicBiomeSource(Holder<Biome> _default, HolderSet<Biome> biomes) {
        this._default = _default;
        this.biomes = biomes;
    }

    @Override
    protected MapCodec<EpicBiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        var biome_stream = biomes.stream();
        return Stream.concat(biome_stream, Stream.of(_default));
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        if (x == 0 && z == 0)
            return this.biomes.get(0);
        else if (x == 4 && z == 0)
            return this.biomes.get(1);

        return this._default;
    }

    public static void registerBiomeSource() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(EpicMod.MOD_ID, "epic_source"), CODEC);
    }
}
