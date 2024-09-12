package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.worldgen.map.MapImage;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public record Clamp(DensityFunction function, int resolution) implements DensityFunction.SimpleFunction {
    public static final MapCodec<Clamp> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DensityFunction.HOLDER_HELPER_CODEC.fieldOf("function").forGetter(Clamp::function),
            Codec.INT.fieldOf("res").forGetter(Clamp::resolution)
    ).apply(instance, Clamp::new));

    @Override
    public double compute(FunctionContext pContext) {
        return this.clamp(this.function.compute(pContext));
    }

    @Override
    public double minValue() {
        return this.clamp(this.function.minValue());
    }

    @Override
    public double maxValue() {
        return this.clamp(this.function.maxValue());
    }

    @Override
    public KeyDispatchDataCodec<Clamp> codec() {
        return new KeyDispatchDataCodec<>(CODEC);
    }

    private double clamp(double value) {
        float scaled = (int) (value * this.resolution) + 1;
        return (scaled / this.resolution);
    }
}
