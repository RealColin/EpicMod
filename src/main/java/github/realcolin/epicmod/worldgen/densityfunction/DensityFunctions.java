package github.realcolin.epicmod.worldgen.densityfunction;

import com.mojang.serialization.MapCodec;
import github.realcolin.epicmod.EpicMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;


public class DensityFunctions {
    private static final DeferredRegister<MapCodec<? extends DensityFunction>> DENSITY_FUNCTIONS = DeferredRegister.create(BuiltInRegistries.DENSITY_FUNCTION_TYPE.key(), EpicMod.MOD_ID);

    public static void register(IEventBus bus) {
        DENSITY_FUNCTIONS.register("image_sampler", () -> ImageSampler.CODEC);

        DENSITY_FUNCTIONS.register(bus);
    }
}
