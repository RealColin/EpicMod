package github.realcolin.epicmod.item;

import github.realcolin.epicmod.EpicMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EpicItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EpicMod.MOD_ID);

    public static final RegistryObject<Item> DIMENSIONAL_STICK = ITEMS.register("dimensional_stick",
            () -> new DimensionalStick(new Item.Properties()));

 ;   public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
