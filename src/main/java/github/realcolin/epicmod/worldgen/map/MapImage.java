package github.realcolin.epicmod.worldgen.map;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import github.realcolin.epicmod.util.ImageWrapper;
import github.realcolin.epicmod.worldgen.biome.EpicBiomeSource;
import github.realcolin.epicmod.worldgen.noise.Perlin;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapImage {
    public static final MapCodec<MapImage> CODEC =
            RecordCodecBuilder.mapCodec(map -> map.group(
                    ResourceLocation.CODEC.fieldOf("image").forGetter(src -> src.res),
                    Biome.CODEC.fieldOf("default_biome").forGetter(src -> src.defaultBiome),
                    MapEntry.ENTRY_CODEC.fieldOf("entries").forGetter(src -> src.entries)
            ).apply(map, MapImage::new));

    private final ResourceLocation res;
    private final Holder<Biome> defaultBiome;
    private final List<MapEntry> entries;
    private final BufferedImage image;
    private EpicBiomeSource source = null;

    private final Perlin perlin = new Perlin(1);

    // TODO remove temp print statements
    public MapImage(ResourceLocation res, Holder<Biome> defaultBiome, List<MapEntry> entries) {
        System.out.println("MapImage constructor called");

        this.res = res;
        this.defaultBiome = defaultBiome;
        this.entries = entries;

        String PATH = "assets/%s/map/%s".formatted(res.getNamespace(), res.getPath());

        System.out.println(PATH);

        URL resource = getClass().getClassLoader().getResource(PATH);
        try {
            image = ImageIO.read(Objects.requireNonNull(resource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public EpicBiomeSource getSource() {
        if (source == null) {
            source = new EpicBiomeSource(this);
        }

        return source;
    }

    public List<MapEntry> getEntries() {
        return entries;
    }

    public Holder<Biome> defaultBiome() {
        return this.defaultBiome;
    }

    public Holder<Biome> getBiome(int x, int z) {
        int scale = 4; // TODO make this not hardcoded
        double jitter = perlin.sample((x + 0.5) / 2, (z + 0.5) / 2);
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
