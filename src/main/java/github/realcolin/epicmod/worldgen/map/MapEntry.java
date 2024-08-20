package github.realcolin.epicmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public record MapEntry(Integer color, Holder<Biome> biome) {
    private static final MapCodec<Integer> COLOR =
            RecordCodecBuilder.mapCodec(yes -> yes.group(
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("color").forGetter(a -> a)
            ).apply(yes, Integer::valueOf));

    public static final Codec<List<MapEntry>> ENTRY_CODEC =
            RecordCodecBuilder.<MapEntry>create(something -> something.group(
                    COLOR.forGetter(src -> src.color),
                    Biome.CODEC.fieldOf("biome").forGetter(src -> src.biome)
            ).apply(something, MapEntry::new)).listOf();
}
