package committee.nova.mods.dg;


import committee.nova.mods.dg.common.crafting.GlobeCraftingRecipe;
import committee.nova.mods.dg.common.block.GlobeBlock;
import committee.nova.mods.dg.common.tile.GlobeBlockEntity;
import committee.nova.mods.dg.common.item.GlobeBlockItem;
import committee.nova.mods.dg.utils.DimensionHelper;
import committee.nova.mods.dg.utils.GlobeSectionManagerServer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CommonClass {
    public static ResourceKey<Level> globeDimension = ResourceKey.create(Registries.DIMENSION, Constants.rl("globe"));
    public static TagKey<Block> BASE_BLOCK_TAG = TagKey.create(Registries.BLOCK, Constants.rl("base_blocks"));
    public static ResourceLocation globeID = Constants.rl("globe");

    public static GlobeBlock globeBlock;
    public static GlobeBlockItem globeBlockItem;
    public static RecipeSerializer<GlobeCraftingRecipe> globeCrafting;
    public static CreativeModeTab globeItemGroup;
    public static GlobeSectionManagerServer managerServer;
    public static BlockEntityType<GlobeBlockEntity> globeBlockEntityType;
    public static DimensionHelper dimensionHelper;

}