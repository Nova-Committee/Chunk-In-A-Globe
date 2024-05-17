package committee.nova.mods.dg.globe;

import committee.nova.mods.dg.CommonClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlobeBlockItem extends BlockItem {
	public GlobeBlockItem(Block block, Properties settings) {
		super(block, settings);
	}

	public ItemStack getWithBase(Block base) {
		ResourceLocation identifier = BuiltInRegistries.BLOCK.getKey(base);
		ItemStack stack = new ItemStack(this);
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.putString("base_block", identifier.toString());
		stack.setTag(compoundTag);

		return stack;
	}


	@Override
	public InteractionResult place(BlockPlaceContext context) {
		if (context.getPlayer().level().dimension().equals(CommonClass.globeDimension)) {
			if (!context.getPlayer().level().isClientSide) {
				context.getPlayer().displayClientMessage(Component.translatable("globedimension.block.error"), false);
			}
			return InteractionResult.FAIL;
		}
		return super.place(context);
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level world, @Nullable Player player, ItemStack stack, BlockState state) {
		if (stack.hasTag() && stack.getOrCreateTag().contains("base_block")) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof GlobeBlockEntity) {
				ResourceLocation identifier = new ResourceLocation(stack.getOrCreateTag().getString("base_block"));
				if (BuiltInRegistries.BLOCK.getOptional(identifier).isPresent()) {
					((GlobeBlockEntity) blockEntity).setBaseBlock(BuiltInRegistries.BLOCK.get(identifier));
				}
				if (stack.getOrCreateTag().contains("globe_id")) {
					((GlobeBlockEntity) blockEntity).setGlobeID(stack.getOrCreateTag().getInt("globe_id"));
				}
			}
		}
		return super.updateCustomBlockEntityTag(pos, world, player, stack, state);
	}

}
