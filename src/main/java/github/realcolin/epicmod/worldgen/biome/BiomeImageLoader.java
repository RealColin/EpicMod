package github.realcolin.epicmod.worldgen.biome;

import github.realcolin.epicmod.EpicMod;
import net.minecraft.resources.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class BiomeImageLoader {
    private final BufferedImage image;

    private final String PATH = "assets/%s/map/map.png".formatted(EpicMod.MOD_ID);

    public BiomeImageLoader(ResourceLocation res) {
        URL resource = getClass().getClassLoader().getResource(PATH);
        try {
            image = ImageIO.read(Objects.requireNonNull(resource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        String path = res.getNamespace() + "/" + res.getPath();
//        System.out.println(path);
//
//        File file = new File(path);
//        try {
//            this.image = ImageIO.read(file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }

    public int getColorAtPixel(int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (x < 0 || x >= width || y < 0 || y >= height) {
            return -1;
        } else {
            int pixelColor = image.getRGB(x, y) & 0x00FFFFFF;
            System.out.println(pixelColor);
            return pixelColor;
        }
    }
}
