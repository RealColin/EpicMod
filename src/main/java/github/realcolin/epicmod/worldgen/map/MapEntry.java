package github.realcolin.epicmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.worldgen.chunk.Terrain;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public record MapEntry(Integer color, Holder<Biome> biome, Holder<Terrain> terrain) {
    public static final Codec<List<MapEntry>> ENTRY_CODEC =
            RecordCodecBuilder.<MapEntry>create(something -> something.group(
                    Codec.INT.fieldOf("color").forGetter(src -> src.color),
                    Biome.CODEC.fieldOf("biome").forGetter(src -> src.biome),
                    Terrain.CODEC.fieldOf("terrain").forGetter(src -> src.terrain)
            ).apply(something, MapEntry::new)).listOf();
}
