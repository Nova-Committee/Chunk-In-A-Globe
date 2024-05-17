package committee.nova.mods.dg.common.net;

import committee.nova.mods.dg.utils.GlobeSection;
import committee.nova.mods.dg.utils.GlobeSectionManagerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

/**
 * SectionUpdatePkt
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午10:29
 */
public class SectionUpdatePkt {
    public int id;
    public boolean inner;
    public boolean blocks;
    public CompoundTag tag;

    public SectionUpdatePkt() {}

    public SectionUpdatePkt(int id, boolean inner, boolean blocks, CompoundTag tag) {
        this.id = id;
        this.inner = inner;
        this.blocks = blocks;
        this.tag = tag;
    }

    public SectionUpdatePkt(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.inner = buf.readBoolean();
        this.blocks = buf.readBoolean();
        this.tag = buf.readAnySizeNbt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeBoolean(inner);
        buf.writeBoolean(blocks);
        buf.writeNbt(tag);
    }

    public static void run(Level level, int id, boolean inner, boolean blocks, CompoundTag tag) {
            final GlobeSection section = GlobeSectionManagerClient.getGlobeSection(id, inner);
            if (blocks) {
                section.fromBlockTag(tag);
            } else {
                section.fromEntityTag(tag, level);
            }
            GlobeSectionManagerClient.provideGlobeSectionUpdate(inner, id, section);
    }
}
