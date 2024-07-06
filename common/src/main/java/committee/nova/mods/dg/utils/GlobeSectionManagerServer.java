package committee.nova.mods.dg.utils;

import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.common.tile.GlobeBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public abstract class GlobeSectionManagerServer {

	public void updateAndSyncToPlayers(GlobeBlockEntity blockEntity, boolean blocks) {
		if (blockEntity.getLevel().isClientSide) {
			return;
		}

		if (blockEntity.getGlobeID() == -1) {
			return;
		}

		ServerLevel serverWorld = (ServerLevel) blockEntity.getLevel();
		List<ServerPlayer> nearbyPlayers = new ArrayList<>();

		for (ServerPlayer player : serverWorld.players()) {
			if (player.distanceToSqr(Vec3.atLowerCornerOf(blockEntity.getBlockPos())) < 64) {
				nearbyPlayers.add(player);
			}
		}

		if (nearbyPlayers.isEmpty()) {
			return;
		}

		GlobeManager.Globe globe = GlobeManager.getInstance(serverWorld).getGlobeByID(blockEntity.getGlobeID());

		ServerLevel updateWorld = serverWorld.getServer().getLevel(blockEntity.isInner() ? blockEntity.getReturnDimType() : CommonClass.globeDimension);

		if (blocks) {
			globe.updateBlockSection(updateWorld, blockEntity.isInner(), blockEntity);
		} else {
			globe.updateEntitySection(updateWorld, blockEntity.isInner(), blockEntity);
			if (globe.getGlobeSection(blockEntity.isInner()).getEntities().isEmpty()) {
				return;
			}
		}

		GlobeSection section = globe.getGlobeSection(blockEntity.isInner());
		syncToPlayers(blockEntity, section, globe, nearbyPlayers, blocks);

	}

	public void syncToPlayers(GlobeBlockEntity blockEntity, GlobeSection section, GlobeManager.Globe globe, List<ServerPlayer> nearbyPlayers, boolean blocks) {}


}
