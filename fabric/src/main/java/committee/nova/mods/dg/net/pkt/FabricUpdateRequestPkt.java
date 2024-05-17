package committee.nova.mods.dg.net.pkt;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntSet;
import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.Constants;
import committee.nova.mods.dg.common.net.UpdateRequestPkt;
import committee.nova.mods.dg.utils.GlobeManager;
import committee.nova.mods.dg.utils.GlobeSection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * FabricUpdateRequestPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:28
 */
public class FabricUpdateRequestPkt extends UpdateRequestPkt {
    public static void run(MinecraftServer server, IntSet updateQueue, ServerPlayer player) {

        server.execute(() -> {
            for (Integer id : updateQueue) {
                updateAndSyncToPlayers(player, id, true);
                updateAndSyncToPlayers(player, id, false);
            }
        });

    }

    public static void updateAndSyncToPlayers(ServerPlayer playerEntity, int globeID, boolean blocks) {
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

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(globe.getId());
        buf.writeBoolean(false);
        if (blocks) {
            buf.writeBoolean(true);
            buf.writeNbt(section.toBlockTag());
        } else {
            buf.writeBoolean(false);
            buf.writeNbt(section.toEntityTag(globe.getGlobeLocation()));
        }

        ClientboundCustomPayloadPacket clientBoundPacket = new ClientboundCustomPayloadPacket(Constants.rl("section_update"), buf);
        playerEntity.connection.send(clientBoundPacket);
    }
}
