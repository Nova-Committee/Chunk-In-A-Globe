package committee.nova.mods.dg.platform;

import committee.nova.mods.dg.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public ResourceLocation getKeyByBlock(Block block) {
        return IPlatformHelper.super.getKeyByBlock(block);
    }

    @Override
    public Block getBlockByKey(ResourceLocation key) {
        return IPlatformHelper.super.getBlockByKey(key);
    }
}
