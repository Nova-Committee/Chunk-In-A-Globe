package committee.nova.mods.dg.common.item;

import committee.nova.mods.dg.client.ForgeGlobeItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * FabricGlobeBlockItem
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/18 下午2:09
 */
public class ForgeGlobeBlockItem extends GlobeBlockItem {
    public ForgeGlobeBlockItem(Block block, Properties settings) {
        super(block, settings);
    }


    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(
                new IClientItemExtensions() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                        return new ForgeGlobeItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()) ;
                    }
                }
        );
    }

}
