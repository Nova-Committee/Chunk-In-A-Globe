package committee.nova.mods.dg.net;

import io.netty.buffer.Unpooled;
import committee.nova.mods.dg.Constants;
import committee.nova.mods.dg.globe.GlobeBlockEntity;
import committee.nova.mods.dg.utils.GlobeManager;
import committee.nova.mods.dg.utils.GlobeSection;
import committee.nova.mods.dg.utils.GlobeSectionManagerServer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

/**
 * FabricGlobeSectionManagerServer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:30
 */
public class FabricGlobeSectionManagerServer extends GlobeSectionManagerServer {
    @Override
    public void syncToPlayers(GlobeBlockEntity blockEntity, GlobeSection section, GlobeManager.Globe globe, List<ServerPlayer> nearbyPlayers, boolean blocks) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(globe.getId());
        buf.writeBoolean(blockEntity.isInner());
        if (blocks) {
            buf.writeBoolean(true);
            buf.writeNbt(section.toBlockTag());
        } else {
            buf.writeBoolean(false);
            buf.writeNbt(section.toEntityTag(blockEntity.isInner() ? blockEntity.getInnerScanPos() : globe.getGlobeLocation()));
        }

        ClientboundCustomPayloadPacket clientBoundPacket = new ClientboundCustomPayloadPacket(Constants.rl("section_update"), buf);
        for (ServerPlayer nearbyPlayer : nearbyPlayers) {
            nearbyPlayer.connection.send(clientBoundPacket);
        }
    }
}
