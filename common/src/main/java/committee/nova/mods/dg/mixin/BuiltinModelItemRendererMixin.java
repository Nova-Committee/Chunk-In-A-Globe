package committee.nova.mods.dg.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.globe.GlobeItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BuiltinModelItemRendererMixin {

	@Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
	private void render(ItemStack stack, ItemDisplayContext $$1, PoseStack matrix, MultiBufferSource vertexConsumerProvider, int light, int overlay, CallbackInfo ci) {
		if (stack.getItem() instanceof BlockItem) {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			if (block == CommonClass.globeBlock) {
				GlobeItemRenderer.render(stack, matrix, vertexConsumerProvider, light, overlay);
				ci.cancel();
			}
		}
	}
}
