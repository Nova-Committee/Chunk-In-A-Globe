package committee.nova.mods.dg.platform.services;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    default Block getBlockByKey(ResourceLocation key){
      return BuiltInRegistries.BLOCK.get(key);
    }

    default ResourceLocation getKeyByBlock(Block block){
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    default EntityType<?> getEntityTypeByKey(ResourceLocation key){
        return BuiltInRegistries.ENTITY_TYPE.get(key);
    }

    default ResourceLocation getKeyByEntityType(EntityType<?> entityType){
        return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
    }
}