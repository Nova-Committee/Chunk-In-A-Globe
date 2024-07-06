package committee.nova.mods.dg.platform;

import committee.nova.mods.dg.platform.services.IPlatformHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public Block getBlockByKey(ResourceLocation key) {
        return ForgeRegistries.BLOCKS.getValue(key);
    }

    @Override
    public ResourceLocation getKeyByBlock(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    @Override
    public EntityType<?> getEntityTypeByKey(ResourceLocation key) {
        return ForgeRegistries.ENTITY_TYPES.getValue(key);
    }

    @Override
    public ResourceLocation getKeyByEntityType(EntityType<?> entityType) {
        return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
    }
}