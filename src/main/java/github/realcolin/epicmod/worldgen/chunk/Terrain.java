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
                    Codec.INT.fieldOf("height").forGetter(src -> src.h)
            ).apply(yes, Terrain::new));

    public static final Codec<Holder<Terrain>> CODEC = RegistryFileCodec.create(EpicRegistries.TERRAIN, DIRECT_CODEC);

    private final int h;

    public Terrain(int h) {
        this.h = h;
    }

    public int getH() {
        return this.h;
    }
}
