package committee.nova.mods.dg.net.pkt;

import committee.nova.mods.dg.common.net.SectionUpdatePkt;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * ForgeSectionUpdatePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:08
 */
public class ForgeSectionUpdatePkt{
    public static void handle(SectionUpdatePkt msg, Supplier<NetworkEvent.Context> ctx){
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                SectionUpdatePkt.run(ctx.get().getSender().level(), msg.id, msg.inner, msg.blocks, msg.tag);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
