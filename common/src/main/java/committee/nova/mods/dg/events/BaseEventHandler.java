package committee.nova.mods.dg.events;

import committee.nova.mods.dg.utils.GlobeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static committee.nova.mods.dg.CommonClass.globeBlock;
import static committee.nova.mods.dg.CommonClass.globeDimension;

/**
 * BaseEventHandler
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午9:28
 */
public class BaseEventHandler {

    public static void onWorldLTickPre(ServerLevel world){
        if (!world.isClientSide && world.dimension().equals(Level.OVERWORLD)) {
            GlobeManager.getInstance(world).tick();
        }
    }

    public static InteractionResult onLeftClickBlock(Level world, BlockPos blockPos){
        if (world.dimension() == globeDimension) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() == globeBlock || state.getBlock() == Blocks.BARRIER) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }
    public static InteractionResult onRightClickBlock(Level world, Player player, InteractionHand hand){
        if (world.dimension() == globeDimension) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() == Items.BARRIER) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

}
