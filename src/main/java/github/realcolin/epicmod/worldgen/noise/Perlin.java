package github.realcolin.epicmod.worldgen.noise;

import java.util.Random;

public class Perlin {

    private final int[] permutation;

    public Perlin(long seed) {
        permutation = new int[512];
        Random random = new Random(seed);

        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int swap = p[i];
            p[i] = p[j];
            p[j] = swap;
        }

        for (int i = 0; i < 256; i++) {
            permutation[i] = permutation[i + 256] = p[i];
        }
    }

    private double noise(double x, double y) {
        int xi = (int) Math.floor(x) & 255;
        int yi = (int) Math.floor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa, ab, ba, bb;
        aa = permutation[permutation[xi] + yi];
        ab = permutation[permutation[xi] + yi + 1];
        ba = permutation[permutation[xi + 1] + yi];
        bb = permutation[permutation[xi + 1] + yi + 1];

        double x1, x2;
        x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
        x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);

        return lerp(x1, x2, v);
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public double sample(double x, double y) {
        double sum = 0.0f;
        double amp = 1.0f;
        double freq = 2.0f;
        double lanc = 2.0f;
        double persistance = 0.125f;

        for (int i = 0; i < 4; i++) {
            sum += amp * noise(x * freq, y * freq);
            amp *= persistance;
            freq *= lanc;
        }

        return sum;
    }
}
