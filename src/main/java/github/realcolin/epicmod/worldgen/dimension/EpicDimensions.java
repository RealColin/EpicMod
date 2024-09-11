package github.realcolin.epicmod.worldgen.dimension;

import github.realcolin.epicmod.EpicMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class EpicDimensions {

    public static final ResourceKey<Level> EPICDIM_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(EpicMod.MOD_ID, "realdim"));
}
