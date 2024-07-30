package github.realcolin.epicmod.item;

import github.realcolin.epicmod.worldgen.dimension.EpicDimensions;
import github.realcolin.epicmod.worldgen.dimension.EpicTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DimensionalStick extends Item {

    public DimensionalStick(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if (pPlayer.level() instanceof ServerLevel level) {
            ResourceKey<Level> resKey = EpicDimensions.EPICDIM_KEY;

            BlockPos pos = new BlockPos((int)pPlayer.position().x, (int)pPlayer.position().y, (int)pPlayer.position().z);

            ServerLevel dim = level.getServer().getLevel(resKey);
            if (dim != null) {
                pPlayer.changeDimension(dim, new EpicTeleporter(pos));
            }
            else {
                System.out.println("Destination server is null.");
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
