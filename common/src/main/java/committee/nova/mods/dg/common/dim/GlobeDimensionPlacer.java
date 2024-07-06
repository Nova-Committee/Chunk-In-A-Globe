package committee.nova.mods.dg.common.dim;

import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.common.tile.GlobeBlockEntity;
import committee.nova.mods.dg.utils.GlobeManager;
import committee.nova.mods.dg.utils.GlobeSection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

public class GlobeDimensionPlacer {

	private int globeId = -1;
	private ResourceKey<Level> returnDimension = null;
	private BlockPos returnPos = null;
	private Block baseBlock = null;

	public GlobeDimensionPlacer() {
	}

	public GlobeDimensionPlacer(int globeId, ResourceKey<Level> dimensionType, BlockPos returnPos, Block baseBlock) {
		this.globeId = globeId;
		this.returnDimension = dimensionType;
		this.returnPos = returnPos;
		this.baseBlock = baseBlock;
	}

	public PortalInfo placeEntity(Entity entity, ServerLevel serverWorld, Direction direction, double v, double v1) {
		if (globeId == -1) {
			throw new RuntimeException("Unknown globe: " + globeId);
		}
		GlobeManager.Globe globe = GlobeManager.getInstance(serverWorld).getGlobeByID(globeId);

		BlockPos globePos = globe.getGlobeLocation();
		BlockPos spawnPos = globePos.offset(8, 1, 8);
		buildGlobe(serverWorld, globePos, spawnPos);

		return new PortalInfo(Vec3.atLowerCornerOf(spawnPos).add(0.5, 0,0.5), new Vec3(0, 0, 0), 0, 0);
	}

	private void buildGlobe(ServerLevel world, BlockPos globePos, BlockPos spawnPos) {
		final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		for (int x = 0; x < GlobeSection.GLOBE_SIZE; x++) {
			for (int y = 0; y < GlobeSection.GLOBE_SIZE; y++) {
				for (int z = 0; z < GlobeSection.GLOBE_SIZE; z++) {
					if (x == 0 || x == GlobeSection.GLOBE_SIZE -1 || y == 0 || y == GlobeSection.GLOBE_SIZE -1 || z == 0 || z == GlobeSection.GLOBE_SIZE -1) {
						mutable.set(globePos.getX() + x, globePos.getY() + y, globePos.getZ() + z);
						world.setBlockAndUpdate(mutable, Blocks.BARRIER.defaultBlockState());
					}

				}
			}
		}

		world.setBlockAndUpdate(spawnPos.below(), CommonClass.globeBlock.defaultBlockState());
		GlobeBlockEntity exitBlockEntity = (GlobeBlockEntity) world.getBlockEntity(spawnPos.below());
		exitBlockEntity.setGlobeID(globeId);
		exitBlockEntity.setBaseBlock(baseBlock);
		if (returnPos != null && returnDimension != null) {
			exitBlockEntity.setReturnPos(returnPos, returnDimension);
		}
	}
}
