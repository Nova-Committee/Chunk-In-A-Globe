package committee.nova.mods.dg.common.net.pkt;

import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.common.net.SectionUpdatePkt;
import committee.nova.mods.dg.common.net.UpdateRequestPkt;
import committee.nova.mods.dg.common.net.NetWorkDispatcher;
import committee.nova.mods.dg.utils.GlobeManager;
import committee.nova.mods.dg.utils.GlobeSection;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * UpdateRequestPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午10:41
 */
public class ForgeUpdateRequestPkt{

    public static void handle(UpdateRequestPkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerPlayer player = ctx.get().getSender();
                for (Integer id : msg.updateQueue) {
                    updateAndSyncToPlayers(ctx, player, id, true);
                    updateAndSyncToPlayers(ctx, player, id, false);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }


    public static void updateAndSyncToPlayers(Supplier<NetworkEvent.Context> ctx, ServerPlayer playerEntity, int globeID, boolean blocks) {
        if (globeID == -1) {
            return;
        }
        ServerLevel serverWorld = (ServerLevel) playerEntity.level();

        GlobeManager.Globe globe = GlobeManager.getInstance(serverWorld).getGlobeByID(globeID);

        ServerLevel updateWorld = serverWorld.getServer().getLevel(CommonClass.globeDimension);

        if (blocks) {
            globe.updateBlockSection(updateWorld, false, null);
        } else {
            globe.updateEntitySection(updateWorld, false, null);
        }

        GlobeSection section = globe.getGlobeSection(false);

        SectionUpdatePkt pkt = new SectionUpdatePkt();
        pkt.id = globe.getId();
        pkt.inner = false;
        if (blocks) {
            pkt.blocks = true;
            pkt.tag = section.toBlockTag();
        } else {
            pkt.blocks = false;
            pkt.tag = section.toEntityTag(globe.getGlobeLocation());
        }
        NetWorkDispatcher.CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()), pkt);
    }


}
