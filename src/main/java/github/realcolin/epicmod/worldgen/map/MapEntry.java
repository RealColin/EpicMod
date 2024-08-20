package github.realcolin.epicmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public record MapEntry(Integer color, Holder<Biome> biome, String terrain) {
    public static final Codec<List<MapEntry>> ENTRY_CODEC =
            RecordCodecBuilder.<MapEntry>create(something -> something.group(
                    Codec.INT.fieldOf("color").forGetter(src -> src.color),
                    Biome.CODEC.fieldOf("biome").forGetter(src -> src.biome),
                    Codec.STRING.fieldOf("terrain").forGetter(src -> src.terrain)
            ).apply(something, MapEntry::new)).listOf();
}
