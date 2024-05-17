package committee.nova.mods.dg.utils;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;

/**
 * FabricDimensionHelper
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/17 上午12:10
 */
public class FabricDimensionHelper extends DimensionHelper{

    @Override
    public void changeDimension(Entity teleported, ServerLevel dimension, PortalInfo target) {
        FabricDimensions.teleport(teleported, dimension, target);
    }
}
