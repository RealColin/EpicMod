package github.realcolin.epicmod.worldgen.noise;

public class NoiseUtil {

    private static final float[][] GRAD = {
            {-1.0f, -1.0f},
            {1.0f, -1.0f},
            {-1.0f, 1.0f},
            {1.0f, 1.0f},
            {0.0f, -1.0f},
            {-1.0f, 0.0f},
            {0.0f, 1.0f},
            {1.0f, 0.0f}
    };

    public static int floor(float a) {
        return (a >= 0.0F) ? ((int) a) : ((int) a - 1);
    }

    public static float interpolate(float a) {
        return a * a * (3.0f - 2.0f * a);
    }

    public static float interpolate(float a, float b, float c) {
        return a + c * (b - a);
    }

    public static float grad(int seed, int x, int y, float xd, float yd) {
        int hash = seed;
        hash ^= 1619 * x;
        hash ^= 31337 * y;
        hash = hash * hash * hash * 60493;
        hash ^= hash >> 13;

        var fort = GRAD[hash & 0x7];

        return xd * fort[0] + yd * fort[1];
    }
}
