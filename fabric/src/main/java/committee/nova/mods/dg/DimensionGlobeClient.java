package committee.nova.mods.dg;

import committee.nova.mods.dg.client.FabricGlobeItemRenderer;
import io.netty.buffer.Unpooled;
import committee.nova.mods.dg.common.net.SectionUpdatePkt;
import committee.nova.mods.dg.client.render.GlobeBlockEntityRenderer;
import committee.nova.mods.dg.common.item.GlobeBlockItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;

import static committee.nova.mods.dg.utils.GlobeSectionManagerClient.*;
import static committee.nova.mods.dg.utils.GlobeSectionManagerClient.innerSelectionMap;

public class DimensionGlobeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BuiltinItemRendererRegistry.INSTANCE.register(CommonClass.globeBlockItem, new FabricGlobeItemRenderer());
		BlockEntityRenderers.register(CommonClass.globeBlockEntityType, GlobeBlockEntityRenderer::new);
		ClientPlayNetworking.registerGlobalReceiver(Constants.rl( "section_update"), (client, packetContext, packetByteBuf, sender) -> {
			final int id = packetByteBuf.readInt();
			final boolean inner = packetByteBuf.readBoolean();
			final boolean blocks = packetByteBuf.readBoolean();
			final CompoundTag data = packetByteBuf.readNbt();

			client.execute(() -> {
				SectionUpdatePkt.run(packetContext.getLevel(), id, inner, blocks, data);
			});


		});
		ClientTickEvents.START_CLIENT_TICK.register( client -> {
			if (client.level == null) {
				//Ensure the state is not transfered across worlds
				updateQueue.clear();
				selectionMap.clear();
				innerSelectionMap.clear();
				return;
			}
			if (client.level.getGameTime() % 20 == 0 || client.player.getMainHandItem().getItem() instanceof GlobeBlockItem) {
				FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

				buf.writeInt(updateQueue.size());
				for (Integer i : updateQueue) {
					buf.writeInt(i);
				}

				ServerboundCustomPayloadPacket serverBoundPacket = new ServerboundCustomPayloadPacket(Constants.rl("update_request"), buf);
				client.player.connection.send(serverBoundPacket);

				updateQueue.clear();
			}
		});
	}
}
