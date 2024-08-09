package github.realcolin.epicmod.worldgen.noise;

public class Perlin {
    private final int seed;

    public Perlin(int seed) {
        this.seed = seed;
    }

    public float sample(float x, float y) {
        int x0 = NoiseUtil.floor(x);
        int y0 = NoiseUtil.floor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        float xs = NoiseUtil.interpolate(x - x0);
        float ys = NoiseUtil.interpolate(y - y0);
        float xd0 = x - x0;
        float yd0 = y - y0;
        float xd1 = xd0 - 1.0F;
        float yd1 = yd0 - 1.0F;
        float xf0 = NoiseUtil.interpolate(NoiseUtil.grad(seed, x0, y0, xd0, yd0), NoiseUtil.grad(seed, x1, y0, xd1, yd0), xs);
        float xf1 = NoiseUtil.interpolate(NoiseUtil.grad(seed, x0, y1, xd0, yd1), NoiseUtil.grad(seed, x1, y1, xd1, yd1), xs);
        return NoiseUtil.interpolate(xf0, xf1, ys);
    }
}
