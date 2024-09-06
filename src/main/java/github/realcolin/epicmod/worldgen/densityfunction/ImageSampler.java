package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.worldgen.map.MapImage;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record ImageSampler(MapImage map) implements DensityFunction.SimpleFunction {
    public static final MapCodec<ImageSampler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MapImage.CODEC.fieldOf("map").forGetter(ImageSampler::map)
    ).apply(instance, ImageSampler::new));

    @Override
    public double compute(FunctionContext functionContext) {
        return -0.30;
    }

    @Override
    public double minValue() {
        return -0.45;
    }

    @Override
    public double maxValue() {
        return -0.15;
    }

    @Override
    public KeyDispatchDataCodec<ImageSampler> codec() {
        return new KeyDispatchDataCodec<>(CODEC);
    }
}
