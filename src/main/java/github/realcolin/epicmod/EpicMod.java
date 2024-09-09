package github.realcolin.epicmod;

import github.realcolin.epicmod.item.EpicItems;
import github.realcolin.epicmod.worldgen.biome.EpicBiomeSource;
import github.realcolin.epicmod.worldgen.chunk.EpicChunkGenerator;
import github.realcolin.epicmod.worldgen.densityfunction.DensityFunctions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EpicMod.MOD_ID)
public class EpicMod
{
    public static final String MOD_ID = "epicmod";

    // private static final Logger LOGGER = LogUtils.getLogger();

    public EpicMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EpicItems.register(modEventBus);
        DensityFunctions.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(EpicRegistries::init);
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.addListener(this::serverEvent);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
           EpicBiomeSource.registerBiomeSource();
           EpicChunkGenerator.registerChunkGenerator();
        });
    }

    private void serverEvent(final TickEvent.PlayerTickEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientModEvents
    {

    }
}
