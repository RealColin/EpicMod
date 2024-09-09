package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.worldgen.map.MapImage;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record ImageSampler(Holder<MapImage> map, float a) implements DensityFunction.SimpleFunction {
    public static final MapCodec<ImageSampler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MapImage.CODEC.fieldOf("map").forGetter(ImageSampler::map),
            Codec.FLOAT.fieldOf("a").forGetter(ImageSampler::a)
    ).apply(instance, ImageSampler::new));

    @Override
    public double compute(FunctionContext functionContext) {
        return a;
    }

    @Override
    public double minValue() {
        return -1.0;
    }

    @Override
    public double maxValue() {
        return 1.0;
    }

    @Override
    public KeyDispatchDataCodec<ImageSampler> codec() {
        return new KeyDispatchDataCodec<>(CODEC);
    }
}
