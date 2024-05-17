package committee.nova.mods.dg.globe;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.mods.dg.utils.GlobeSectionManagerClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class GlobeItemRenderer {

	public static void render(ItemStack stack, PoseStack matrix, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
		Block baseBlock = Blocks.BLUE_CONCRETE;
		if (stack.hasTag() && stack.getOrCreateTag().contains("base_block")) {
			ResourceLocation blockID = new ResourceLocation(stack.getOrCreateTag().getString("base_block"));
			if (BuiltInRegistries.BLOCK.getOptional(blockID).isPresent()) {
				baseBlock = BuiltInRegistries.BLOCK.get(blockID);
			}
		}
		GlobeBlockEntityRenderer.renderBase(baseBlock, matrix, vertexConsumerProvider, light, overlay);
		if (stack.hasTag() && stack.getOrCreateTag().contains("globe_id")) {
			int globeId = stack.getOrCreateTag().getInt("globe_id");
			GlobeBlockEntityRenderer.renderGlobe(false, globeId, matrix, vertexConsumerProvider, light);
			GlobeSectionManagerClient.requestGlobeUpdate(globeId);
		}
	}

}
