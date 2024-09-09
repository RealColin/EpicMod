package github.realcolin.epicmod.worldgen.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import github.realcolin.epicmod.worldgen.map.MapEntry;
import github.realcolin.epicmod.worldgen.map.MapImage;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class EpicBiomeSource extends BiomeSource {
    public static final MapCodec<EpicBiomeSource> CODEC =
            RecordCodecBuilder.mapCodec(yes -> yes.group(
                    MapImage.CODEC.fieldOf("map").forGetter(src -> src.map)
            ).apply(yes, yes.stable(EpicBiomeSource::new)));

    private final Holder<MapImage> map;

    public EpicBiomeSource(Holder<MapImage> map) {
        this.map = map;
    }

    @Override
    protected @NotNull MapCodec<EpicBiomeSource> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull Stream<Holder<Biome>> collectPossibleBiomes() {
        return map.get().getEntries().stream().map(MapEntry::biome);
    }

    @Override
    public @NotNull Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.@NotNull Sampler sampler) {
        return map.get().getBiome(x, z);
    }

    public static void registerBiomeSource() {
        Registry.register(BuiltInRegistries.BIOME_SOURCE, new ResourceLocation(EpicMod.MOD_ID, "epic_source"), CODEC);
    }
}
