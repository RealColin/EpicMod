package github.realcolin.epicmod.worldgen.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

public class Terrain {

    // TODO implement this

    public static final Codec<Terrain> DIRECT_CODEC =
            RecordCodecBuilder.create(yes -> yes.group(
                    Codec.FLOAT.fieldOf("erosion").forGetter(src -> src.erosion),
                    Codec.FLOAT.fieldOf("continents").forGetter(src -> src.continents),
                    Codec.FLOAT.fieldOf("ridges").forGetter(src -> src.ridges)
                    ).apply(yes, Terrain::new));

    public static final Codec<Holder<Terrain>> CODEC = RegistryFileCodec.create(EpicRegistries.TERRAIN, DIRECT_CODEC);

    private final float erosion;
    private final float continents;
    private final float ridges;

    public Terrain(float erosion, float continents, float ridges) {
        this.erosion = erosion;
        this.continents = continents;
        this.ridges = ridges;
    }
}
