package committee.nova.mods.dg.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;

import java.util.Set;

/**
 * FabricDimensionHelper
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/17 上午12:10
 */
public class ForgeDimensionHelper extends DimensionHelper{

    @Override
    public void changeDimension(Entity teleported, ServerLevel dimension, PortalInfo target) {
        teleported.teleportTo(dimension, target.pos.x, target.pos.y, target.pos.z, Set.of(), target.yRot, target.xRot);
    }
}
