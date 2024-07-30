package github.realcolin.epicmod.worldgen.biome;

import com.mojang.serialization.MapCodec;
import github.realcolin.epicmod.EpicMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

public class EpicBiomeSource extends BiomeSource {

    public static final MapCodec<EpicBiomeSource> CODEC =
            Biome.CODEC.fieldOf("main")
                    .xmap(EpicBiomeSource::new, (epic) -> epic.main)
                    .stable();

    private final Holder<Biome> main;
    //private final Holder<Biome> second;

    public EpicBiomeSource(Holder<Biome> main) {
        this.main = main;
        //this.second = second;
    }

    @Override
    protected MapCodec<EpicBiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.of(this.main);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        return this.main;
    }

    public static void registerBiomeSource() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(EpicMod.MOD_ID, "epic_source"), CODEC);
    }
}
