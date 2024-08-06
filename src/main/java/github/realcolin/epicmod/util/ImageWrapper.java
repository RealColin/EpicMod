package github.realcolin.epicmod.util;

import github.realcolin.epicmod.EpicMod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ImageWrapper {
    private final BufferedImage image;

    public ImageWrapper(String name) {
        String PATH = "assets/%s/map/%s.png".formatted(EpicMod.MOD_ID, name);
        URL resource = getClass().getClassLoader().getResource(PATH);
        System.out.println(resource);
        try {
            image = ImageIO.read(Objects.requireNonNull(resource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getColorAtPixel(int x, int y) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (x < 0 || x >= width || y < 0 || y >= height) {
            return -1;
        } else {
            return image.getRGB(x, y) & 0x00FFFFFF;
        }
    }
}
