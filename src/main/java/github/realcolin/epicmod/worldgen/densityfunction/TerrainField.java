package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import github.realcolin.epicmod.worldgen.chunk.Terrain;
import net.minecraft.util.StringRepresentable;

public enum TerrainField implements StringRepresentable {
    CONTINENTS("continents") {
        @Override
        public float read(Terrain terrain) {
            return terrain.continents();
        }
    },
    EROSION("erosion") {
        @Override
        public float read(Terrain terrain) {
            return terrain.erosion();
        }
    },
    RIDGES("ridges") {
        @Override
        public float read(Terrain terrain) {
            return terrain.ridges();
        }
    },
    TEMPERATURE("temperature") {
        @Override
        public float read(Terrain terrain) {
            return -0.3f;
        }
    };

    public static final Codec<TerrainField> CODEC = StringRepresentable.fromEnum(TerrainField::values);
    private String name;

    private TerrainField(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public abstract float read(Terrain terrain);
}
