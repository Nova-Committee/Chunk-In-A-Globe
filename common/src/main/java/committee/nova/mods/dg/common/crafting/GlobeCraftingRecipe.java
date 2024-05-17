package committee.nova.mods.dg.common.crafting;

import committee.nova.mods.dg.CommonClass;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class GlobeCraftingRecipe extends CustomRecipe {

	private int[] glassSlots = new int[]{0, 1, 2, 3, 5};
	private int[] blockSlots = new int[]{6, 7, 8};

	public GlobeCraftingRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		return !assemble(inv, null).isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingContainer inv, RegistryAccess manager) {
		for (int glassSlot : glassSlots) {
			if (inv.getItem(glassSlot).getItem() != Items.GLASS) {
				return ItemStack.EMPTY;
			}
		}
		if (!inv.getItem(4).isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack blockStack = ItemStack.EMPTY;
		for (int blockSlot : blockSlots) {
			if (!blockStack.isEmpty()) {
				if (blockStack.getItem() != inv.getItem(blockSlot).getItem()) {
					return ItemStack.EMPTY;
				}
			}
			blockStack = inv.getItem(blockSlot);
			if (blockStack.isEmpty()) {
				return ItemStack.EMPTY;
			}
			if (blockStack.getItem() instanceof BlockItem) {
				Block block = ((BlockItem) blockStack.getItem()).getBlock();
				if (!block.defaultBlockState().is(CommonClass.BASE_BLOCK_TAG)) {
					return ItemStack.EMPTY;
				}
			} else {
				return ItemStack.EMPTY;
			}
		}
		Block block = ((BlockItem) blockStack.getItem()).getBlock();
		return CommonClass.globeBlockItem.getWithBase(block);
	}
	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 3 && height >= 3;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return CommonClass.globeCrafting;
	}
}
