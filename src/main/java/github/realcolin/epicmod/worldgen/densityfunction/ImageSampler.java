package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.worldgen.terrain.Terrain;
import github.realcolin.epicmod.worldgen.map.MapImage;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.jetbrains.annotations.NotNull;

public record ImageSampler(Holder<MapImage> map, TerrainField field) implements DensityFunction.SimpleFunction {
    public static final MapCodec<ImageSampler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MapImage.CODEC.fieldOf("map").forGetter(ImageSampler::map),
            TerrainField.CODEC.fieldOf("field").forGetter(ImageSampler::field)
    ).apply(instance, ImageSampler::new));

    @Override
    public double compute(FunctionContext functionContext) {
        Terrain terrain = map.get().getTerrain(functionContext.blockX(), functionContext.blockZ());

        return field.read(terrain);
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
    public @NotNull KeyDispatchDataCodec<ImageSampler> codec() {
        return new KeyDispatchDataCodec<>(CODEC);
    }
}
