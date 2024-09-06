package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.DensityFunction;

public class DensityFunctions {
    public static void init() {
        //register("temperature", ImageSampler.CODEC);
    }

    private static void register(String name, Codec<? extends DensityFunction> type) {
        // TODO implement this
    }
}
