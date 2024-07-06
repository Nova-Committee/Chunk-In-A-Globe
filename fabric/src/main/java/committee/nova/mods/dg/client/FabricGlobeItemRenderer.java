package committee.nova.mods.dg.client;

import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.mods.dg.client.render.GlobeItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * FabricGlobeItemRenderer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/18 下午1:48
 */
public class FabricGlobeItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        GlobeItemRenderer.render(stack, matrices, vertexConsumers, light, overlay);
    }

}
