package committee.nova.mods.dg.common.net;

import committee.nova.mods.dg.common.tile.GlobeBlockEntity;
import committee.nova.mods.dg.utils.GlobeManager;
import committee.nova.mods.dg.utils.GlobeSection;
import committee.nova.mods.dg.utils.GlobeSectionManagerServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

/**
 * FabricGlobeSectionManagerServer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:30
 */
public class ForgeGlobeSectionManagerServer extends GlobeSectionManagerServer {
    @Override
    public void syncToPlayers(GlobeBlockEntity blockEntity, GlobeSection section, GlobeManager.Globe globe, List<ServerPlayer> nearbyPlayers, boolean blocks) {
        SectionUpdatePkt pkt = new SectionUpdatePkt();
        pkt.id = globe.getId();
        pkt.inner = blockEntity.isInner();
        if (blocks) {
            pkt.blocks = true;
            pkt.tag = section.toBlockTag();
        } else {
            pkt.blocks = false;
            pkt.tag = section.toEntityTag(blockEntity.isInner() ? blockEntity.getInnerScanPos() : globe.getGlobeLocation());
        }
        for (ServerPlayer player : nearbyPlayers) {
            NetWorkDispatcher.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), pkt);
        }
    }
}
