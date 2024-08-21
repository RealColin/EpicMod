package github.realcolin.epicmod;

import github.realcolin.epicmod.worldgen.chunk.Terrain;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataPackRegistryEvent;

public class EpicRegistries {

    // Regular Registries
    public static final ResourceKey<Registry<Terrain>> TERRAIN = createRegistryKey("worldgen/terrain");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(new ResourceLocation(name));
    }

    public static void init(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(TERRAIN, Terrain.DIRECT_CODEC, Terrain.DIRECT_CODEC);
    }
}
