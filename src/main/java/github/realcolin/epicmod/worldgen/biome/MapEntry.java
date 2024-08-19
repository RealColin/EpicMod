package github.realcolin.epicmod.worldgen.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.worldgen.chunk.TerrainType;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public record MapEntry(Integer color, Holder<Biome> biome) {
    private static final MapCodec<Integer> COLOR =
            RecordCodecBuilder.mapCodec(yes -> yes.group(
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("color").forGetter(a -> a)
            ).apply(yes, Integer::valueOf));

    public static final Codec<MapEntry> ENTRY_CODEC =
            RecordCodecBuilder.create(something -> something.group(
                    COLOR.forGetter(src -> src.color),
                    Biome.CODEC.fieldOf("biome").forGetter(src -> src.biome)
            ).apply(something, MapEntry::new));
}
