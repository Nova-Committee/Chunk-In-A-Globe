package committee.nova.mods.dg.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static committee.nova.mods.dg.client.render.GlobeItemRenderer.render;

/**
 * ForgeGlobeItemRenderer
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/18 下午2:25
 */
public class ForgeGlobeItemRenderer extends BlockEntityWithoutLevelRenderer {
    public ForgeGlobeItemRenderer(BlockEntityRenderDispatcher renderDispatcher, EntityModelSet modelSet) {
        super(renderDispatcher, modelSet);
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int i, int z) {
        render(stack, poseStack, source, i, z);
    }


}
