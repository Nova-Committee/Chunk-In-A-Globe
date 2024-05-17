package committee.nova.mods.dg.common.net;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.utils.GlobeManager;
import committee.nova.mods.dg.utils.GlobeSection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;


/**
 * UpdateRequestPkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午10:41
 */
public class UpdateRequestPkt {
    public int amount;
    public IntSet updateQueue;

    public UpdateRequestPkt() {}

    public UpdateRequestPkt(int amount, IntSet updateQueue) {
        this.amount = amount;
        this.updateQueue = updateQueue;
    }

    public UpdateRequestPkt(FriendlyByteBuf buf) {
        this.amount = buf.readInt();
        IntSet updateQueue = new IntOpenHashSet();
        for (int i = 0; i < amount; i++) {
            updateQueue.add(buf.readInt());
        }
        this.updateQueue = updateQueue;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(amount);
        for (Integer i : updateQueue) {
            buf.writeInt(i);
        }
    }



}
