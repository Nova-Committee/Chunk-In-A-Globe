package committee.nova.mods.dg.globe;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.Constants;
import committee.nova.mods.dg.utils.GlobeSection;
import committee.nova.mods.dg.utils.GlobeSectionManagerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;

import java.util.*;

public class GlobeBlockEntityRenderer implements BlockEntityRenderer<GlobeBlockEntity> {

	private static int renderDepth = 0;


	@Override
	public void render(GlobeBlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final boolean inner = blockEntity.getLevel().dimension().equals(CommonClass.globeDimension);
		renderGlobe(inner, blockEntity.getGlobeID(), matrices, vertexConsumers, light);
		if (inner) {
			matrices.pushPose();
			matrices.translate(0, 1, 0);
			renderBase(null, matrices, vertexConsumers, light, overlay);
			matrices.popPose();

			matrices.pushPose();
			matrices.translate(-7.5, 0, -7.5);
			matrices.scale(16F, 16F, 16F);
			renderBase(blockEntity.getBaseBlock(), matrices, vertexConsumers, light, overlay);
			matrices.popPose();
		} else {
			renderBase(blockEntity.getBaseBlock(), matrices, vertexConsumers, light, overlay);
		}
	}

	public static void renderGlobe(boolean inner, int globeID, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		if (renderDepth > 2) {
			return;
		}
		ProfilerFiller profiler = Minecraft.getInstance().getProfiler();
		profiler.push("Globe renderer");
		renderDepth ++;
		if (globeID != -1) {
			final float scale = inner ? 16F : 1 / 16F;

			final BlockRenderDispatcher renderManager = Minecraft.getInstance().getBlockRenderer();
			final GlobeSection section = GlobeSectionManagerClient.getGlobeSection(globeID, inner);
			matrices.pushPose();
			if (inner) {
				matrices.translate(-8 * scale, -8 * scale, -8 * scale);
				matrices.translate(-7.5, 0, -7.5);
			} else {
				matrices.translate(-1 / 32F, 0, -1/32F);
			}

			matrices.scale(scale, scale, scale);
			profiler.push("blocks");
			for (Map.Entry<BlockPos, BlockState> entry : section.getStateMap().entrySet()) {
				matrices.pushPose();
				matrices.translate(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
				if (entry.getValue().getBlock() instanceof GlobeBlock) {
//					BlockPos checkPos = entry.getKey().subtract(new Vec3i(8, 8 , 8));
//					if (checkPos.getX() != 0 && checkPos.getY() != 0 && checkPos.getZ() != 0) {
//						renderGlobe(false, section.getGlobeData().get(entry.getKey()), matrices, vertexConsumers, light);
//					}

				} else {
					renderManager.renderSingleBlock(entry.getValue(), matrices, vertexConsumers, light, OverlayTexture.NO_OVERLAY);
				}
				matrices.popPose();
			}
			profiler.pop();

			profiler.push("entities");
			for (Entity entity : section.getEntities()) {
				Vec3 position = section.getEntityVec3dMap().get(entity);

				matrices.pushPose();

				if (inner) {
					matrices.translate(position.x, position.y, position.z);
				} else {
					matrices.translate(position.x, position.y, position.z);
				}

				entity.setPos(0, 0, 0);
				entity.xo = 0;
				entity.yo = 0;
				entity.zo = 0;
				Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, entity.getYRot(), 1, matrices, vertexConsumers, light);
				matrices.popPose();
			}
			matrices.popPose();
			profiler.pop();
		}
		profiler.pop();
		renderDepth --;
	}

	public static void renderBase(Block baseBlock, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final BlockRenderDispatcher renderManager = Minecraft.getInstance().getBlockRenderer();

		matrices.pushPose();
		ResourceLocation blockTexture = new ResourceLocation(Constants.MOD_ID, "textures/blocks/portal.png");
		TextureAtlasSprite blockSprite;
		if (baseBlock != null) {
			BakedModel bakedModel = renderManager.getBlockModel(baseBlock.defaultBlockState());
			blockSprite =  bakedModel.getParticleIcon();
			blockTexture = blockSprite.atlasLocation();
		} else {
			blockSprite =  renderManager.getBlockModel(Blocks.STONE.defaultBlockState()).getParticleIcon();
		}
		BaseModel baseModel = new BaseModel(blockSprite);
		baseModel.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.entitySolid(blockTexture)), light, overlay, 1F, 1F, 1F, 1F);
		matrices.popPose();
	}

	private static class BaseModel extends Model {

		private final ModelPart base;

		public BaseModel(TextureAtlasSprite sprite) {
			super(RenderType::entityCutoutNoCull);

			List<ModelPart.Cube> cuboids = new ArrayList<>();
			Map<String, ModelPart> children = new HashMap<>();
			int width = Math.round((sprite.getX() + sprite.contents().width()) / sprite.getU1());
			int height = Math.round((sprite.getY()+ sprite.contents().height()) / sprite.getV1());

			ModelPart.Cube cuboid = new ModelPart.Cube(sprite.getX()-32, sprite.getY(), // WHY??
					0, 0, 0,
					16, 1, 16,
					0, 0, 0,
					false, width, height,
					EnumSet.allOf(Direction.class));
			cuboids.add(cuboid);

			base = new ModelPart(cuboids, children);
		}

		@Override
		public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
			base.render(matrices, vertexConsumer, light, overlay);
		}
	}
}
