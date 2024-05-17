package committee.nova.mods.dg.globe;

import committee.nova.mods.dg.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GlobeBlock extends BaseEntityBlock {
	public GlobeBlock() {
		super(Properties.of().noOcclusion());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GlobeBlockEntity(pos, state);
	}
	//@Override
	//public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
	//	return true;
	//}


	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof GlobeBlockEntity) {
				((GlobeBlockEntity) blockEntity).transportPlayer((ServerPlayer) player);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		popResource(world, pos, getDroppedStack(world, pos));
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState $$2) {
		return getDroppedStack(world, pos);
	}

	private ItemStack getDroppedStack(BlockGetter world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof GlobeBlockEntity) {
			ItemStack stack = CommonClass.globeBlockItem.getWithBase(((GlobeBlockEntity) blockEntity).getBaseBlock());
			stack.getOrCreateTag().putInt("globe_id", ((GlobeBlockEntity) blockEntity).getGlobeID());
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, CommonClass.globeBlockEntityType, GlobeBlockEntity::tick);
	}


}
