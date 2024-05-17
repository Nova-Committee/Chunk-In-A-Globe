package committee.nova.mods.dg;

import committee.nova.mods.dg.common.net.UpdateRequestPkt;
import committee.nova.mods.dg.events.BaseEventHandler;
import committee.nova.mods.dg.globe.GlobeBlockItem;
import committee.nova.mods.dg.net.NetWorkDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static committee.nova.mods.dg.utils.GlobeSectionManagerClient.*;

/**
 * ModEvents
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:47
 */
@Mod.EventBusSubscriber
public class ModEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event){
        if (Minecraft.getInstance().level == null) {
            //Ensure the state is not transfered across worlds
            updateQueue.clear();
            selectionMap.clear();
            innerSelectionMap.clear();
            return;
        }
        if (Minecraft.getInstance().level.getGameTime() % 20 == 0 || Minecraft.getInstance().player.getMainHandItem().getItem() instanceof GlobeBlockItem) {

            UpdateRequestPkt pkt = new UpdateRequestPkt();
            pkt.amount = updateQueue.size();
            pkt.updateQueue = updateQueue;
            NetWorkDispatcher.CHANNEL.sendToServer(pkt);
            updateQueue.clear();
        }
    }

    @SubscribeEvent
    public static void onWorldLTickPre(TickEvent.LevelTickEvent event){
        if(event.side.isServer()){
            if(event.phase == TickEvent.Phase.START && event.level instanceof ServerLevel serverLevel){
                BaseEventHandler.onWorldLTickPre(serverLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event){
        if (Objects.requireNonNull(BaseEventHandler.onLeftClickBlock(event.getLevel(), event.getPos())) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        if (Objects.requireNonNull(BaseEventHandler.onRightClickBlock(event.getLevel(), event.getEntity(), event.getHand())) == InteractionResult.FAIL) {
            event.setCanceled(true);
        }
    }

}
