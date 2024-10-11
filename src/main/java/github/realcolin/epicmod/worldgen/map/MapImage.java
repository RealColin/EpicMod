package github.realcolin.epicmod.worldgen.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicRegistries;
import github.realcolin.epicmod.worldgen.terrain.Terrain;
import github.realcolin.epicmod.worldgen.noise.Perlin;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MapImage {
    public static final Codec<MapImage> DIRECT_CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("image").forGetter(src -> src.res),
                    Biome.CODEC.fieldOf("default_biome").forGetter(src -> src.defaultBiome),
                    Terrain.CODEC.fieldOf("default_terrain").forGetter(src -> src.defaultTerrain),
                    MapEntry.ENTRY_CODEC.fieldOf("entries").forGetter(src -> src.entries)
            ).apply(instance, MapImage::new));

    public static final Codec<Holder<MapImage>> CODEC = RegistryFileCodec.create(EpicRegistries.MAP, DIRECT_CODEC);


    private final ResourceLocation res;
    private final Holder<Biome> defaultBiome;
    private final Holder<Terrain> defaultTerrain;
    private final List<MapEntry> entries;
    private final BufferedImage image;

    private final int scaleFactor = 16;

    private final Perlin biome_jitter = new Perlin(0);

    private final Perlin jitterNoise = new Perlin(3);

    public MapImage(ResourceLocation res, Holder<Biome> defaultBiome, Holder<Terrain> defaultTerrain, List<MapEntry> entries) {
        this.res = res;
        this.defaultBiome = defaultBiome;
        this.defaultTerrain = defaultTerrain;
        this.entries = entries;

        String PATH = "assets/%s/map/%s".formatted(res.getNamespace(), res.getPath());

        URL resource = getClass().getClassLoader().getResource(PATH);
        try {
            image = ImageIO.read(Objects.requireNonNull(resource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<MapEntry> getEntries() {
        return entries;
    }

    public Holder<Biome> getBiome(int x, int z) {
        int scale = 4; // TODO make this not hardcoded
        double jitter = biome_jitter.sample((x + 0.5) / 2, (z + 0.5) / 2);
        int rounded = (int)Math.round(jitter * scale);

        int color = this.getColorAtPixel((x + rounded) / scale, (z + rounded) / scale);

        if (color != -1) {
            for (MapEntry entry : entries) {
                if (entry.color() == color)
                    return entry.biome();
            }
        }
        return this.defaultBiome;
    }

    public Terrain getTerrain(int block_x, int block_z) {
        double jitter = jitterNoise.sample((block_x + 0.01) * 0.05, (block_z + 0.01) * 0.05) * 4.0;

        int cx = (int) ((block_x + jitter) / scaleFactor);
        int cz = (int) ((block_z + jitter) / scaleFactor);
        int ix = (int)(block_x + jitter) % scaleFactor;
        int iz = (int)(block_z + jitter) % scaleFactor;

        int base = this.getColorAtPixel(cx, cz);
        int left = this.getColorAtPixel(cx - 1, cz);
        int right = this.getColorAtPixel(cx + 1, cz);
        int up = this.getColorAtPixel(cx, cz - 1);
        int down = this.getColorAtPixel(cx, cz + 1);

        int half = scaleFactor / 2;
        int color = base;

        if (ix < half && left != base) {
            if (iz < half && up != base) {
                if (ix + iz < half) {
                    if (left == up) {
                        color = left;
                    } else {
                        int corner = this.getColorAtPixel(cx - 1, cz - 1);

                        if (corner == left || corner == up) {
                            color = corner;
                        }
                    }
                }
            } else if (iz > half && down != base) {
                if (ix + iz > half + (ix * 2)) {
                    if (left == down) {
                        color = left;
                    } else {
                        int corner = this.getColorAtPixel(cx - 1, cz + 1);

                        if (corner == left || corner == down) {
                            color = corner;
                        }
                    }
                }
            }
        } else if (ix >= half && right != base) {
            if (iz < half && up != base) {
                if (ix + iz >= half + (iz * 2)) {
                    if (right == up) {
                        color = right;
                    } else {
                        int corner = this.getColorAtPixel(cx + 1, cz - 1);

                        if (corner == right || corner == up) {
                            color = corner;
                        }
                    }
                }
            } else if (iz > half && down != base) {
                if (ix + iz >= half * 3) {
                    if (right == down) {
                        color = right;
                    } else {
                        int corner = this.getColorAtPixel(cx + 1, cz + 1);

                        if (corner == right || corner == down) {
                            color = corner;
                        }
                    }
                }
            }
        }

        if (color != -1) {
            for (MapEntry entry : entries) {
                if (entry.color() == color)
                    return entry.terrain().get();
            }
        }

        return this.defaultTerrain.get();
    }


    private int getColorAtPixel(int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (x < 0 || x >= width || y < 0 || y >= height) {
            return -1;
        } else {
            return image.getRGB(x, y) & 0x00FFFFFF;
        }
    }
}
