package committee.nova.mods.dg.globe;

import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.common.dim.ExitPlacer;
import committee.nova.mods.dg.common.dim.GlobeDimensionPlacer;
import committee.nova.mods.dg.utils.DimensionHelper;
import committee.nova.mods.dg.utils.GlobeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlobeBlockEntity extends BlockEntity {

	private int globeID = -1;
	private Block baseBlock;

	private BlockPos returnPos;
	private ResourceKey<Level> returnDimType;

	public GlobeBlockEntity(BlockPos pos, BlockState state) {
		super(CommonClass.globeBlockEntityType, pos, state);
	}

	public static void tick(Level world, BlockPos pos, BlockState state, GlobeBlockEntity entity) {
		if (!world.isClientSide && entity.globeID != -1) {
			if (!entity.isInner()) {
				GlobeManager.getInstance((ServerLevel) world)
						.markGlobeForTicking(entity.globeID);
			}
		}
		if (!world.isClientSide) {
			if (world.getGameTime() % 20 == 0) {
				CommonClass.managerServer.updateAndSyncToPlayers(entity, true);
			} else {
				CommonClass.managerServer.updateAndSyncToPlayers(entity, false);
			}
		}
	}

	@Override
	 public void load(CompoundTag tag) {
		super.load(tag);
		globeID = tag.getInt("globe_id");
		if (tag.contains("base_block")) {
			ResourceLocation identifier = new ResourceLocation(tag.getString("base_block"));
			if (BuiltInRegistries.BLOCK.getOptional(identifier).isPresent()) {
				baseBlock = BuiltInRegistries.BLOCK.get(identifier);
			}
		}
		if (tag.contains("return_x")) {
			returnPos = new BlockPos(tag.getInt("return_x"), tag.getInt("return_y"), tag.getInt("return_z"));

			ResourceLocation returnType = new ResourceLocation(tag.getString("return_dim"));
			returnDimType = ResourceKey.create(Registries.DIMENSION, returnType);
		}
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.putInt("globe_id", globeID);
		if (baseBlock != null) {
			tag.putString("base_block", BuiltInRegistries.BLOCK.getKey(baseBlock).toString());
		}

		if (returnPos != null && returnDimType != null) {
			tag.putInt("return_x", returnPos.getX());
			tag.putInt("return_y", returnPos.getY());
			tag.putInt("return_z", returnPos.getZ());
			tag.putString("return_dim", returnDimType.registry().toString());
		}

		super.saveAdditional(tag);
	}


	private void newGlobe() {
		if (level.isClientSide) {
			throw new RuntimeException();
		}

		globeID = GlobeManager.getInstance((ServerLevel) level).getNextGlobe().getId();
		setChanged();
		//TODO sync();
	}

	public void transportPlayer(ServerPlayer playerEntity) {
		if (level.isClientSide) {
			throw new RuntimeException();
		}

		if (playerEntity.level().dimension() == CommonClass.globeDimension) {
			transportPlayerOut(playerEntity);
		} else {
			if (globeID == -1) {
				newGlobe();
			}
			ServerLevel targetWorld = playerEntity.getServer().getLevel(CommonClass.globeDimension);
			GlobeDimensionPlacer placer = new GlobeDimensionPlacer(globeID, level.dimension(), getBlockPos(), baseBlock);
			CommonClass.dimensionHelper.changeDimension(playerEntity, targetWorld, placer.placeEntity(playerEntity, targetWorld, Direction.NORTH, 0, 0));
		}
	}

	public void setReturnPos(BlockPos returnPos, ResourceKey<Level> returnDimType) {
		this.returnPos = returnPos;
		this.returnDimType = returnDimType;
		setChanged();
	}

	public void transportPlayerOut(ServerPlayer playerEntity) {
		if (getLevel().dimension() == CommonClass.globeDimension) {
			ResourceKey<Level> teleportDim = returnDimType == null ? Level.OVERWORLD : returnDimType;
			ServerLevel world = playerEntity.getServer().getLevel(teleportDim);
			ExitPlacer placer = new ExitPlacer(returnPos);
			CommonClass.dimensionHelper.changeDimension(playerEntity, world, placer.placeEntity(playerEntity, world, Direction.NORTH, 0, 0));
		}
	}

	public ResourceKey<Level> getReturnDimType() {
		return returnDimType == null ? Level.OVERWORLD : returnDimType;
	}

	public BlockPos getInnerScanPos() {
		if (returnPos == null) {
			return BlockPos.ZERO;
		}
		return returnPos.subtract(new Vec3i(8, 8, 8));
	}

	public boolean isInner() {
		return getLevel().dimension() == CommonClass.globeDimension;
	}

	public int getGlobeID() {
		return globeID;
	}

	public void setGlobeID(int globeID) {
		this.globeID = globeID;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public @NotNull CompoundTag getUpdateTag() {
		CompoundTag tag = new CompoundTag();
		saveAdditional(tag);
		return tag;
	}


	public Block getBaseBlock() {
		if (baseBlock == null) {
			return Blocks.OAK_PLANKS;
		}
		return baseBlock;
	}

	public void setBaseBlock(Block baseBlock) {
		this.baseBlock = baseBlock;
		setChanged();
	}
}
