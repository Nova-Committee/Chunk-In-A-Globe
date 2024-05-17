package committee.nova.mods.dg.common.dim;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ExitPlacer {

	private BlockPos blockPos;

	public ExitPlacer(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	public PortalInfo placeEntity(Entity teleported, ServerLevel destination, Direction portalDir, double horizontalOffset, double verticalOffset) {
		if (blockPos == null) {
			if (teleported instanceof Player) {
				blockPos = getBedLocation((ServerPlayer) teleported, destination);
			}
			if (blockPos == null) {
				blockPos = destination.getSharedSpawnPos();
			}
		}
		return new PortalInfo(Vec3.atLowerCornerOf(blockPos), new Vec3(0, 0, 0), 0, 0);
	}

	private static BlockPos getBedLocation(ServerPlayer playerEntity, ServerLevel destination) {
		Optional<BlockPos> bedLocation = playerEntity.getSleepingPos();
		return bedLocation.flatMap(pos -> Player.findRespawnPositionAndUseSpawnBlock(destination, pos, 0, playerEntity.isRespawnForced(), true)
				.map(BlockPos::containing))
				.orElse(null);
	}
}
