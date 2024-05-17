package committee.nova.mods.dg;

import committee.nova.mods.dg.globe.GlobeBlock;
import committee.nova.mods.dg.globe.GlobeBlockEntity;
import committee.nova.mods.dg.globe.GlobeBlockItem;
import committee.nova.mods.dg.globe.VoidChunkGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static committee.nova.mods.dg.CommonClass.globeID;

/**
 * ModRegistries
 *
 * @author cnlimiter
 * @version 1.0
 * @description
 * @date 2024/5/16 下午11:38
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);
    //private static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = DeferredRegister.create(Registries.CHUNK_GENERATOR, Constants.MOD_ID);

    public static RegistryObject<GlobeBlock> globeBlock1 = BLOCKS.register("globe", GlobeBlock::new);
    public static RegistryObject<GlobeBlockItem> globeBlockItem1 = ITEMS.register("globe", () ->  new GlobeBlockItem(globeBlock1.get(), new Item.Properties()));
    public static RegistryObject<BlockEntityType<GlobeBlockEntity>> globeBlockEntityType1 = blockEntity("globe", GlobeBlockEntity::new, () -> new Block[]{globeBlock1.get()});
    //public static RegistryObject<RecipeSerializer<GlobeCraftingRecipe>> globeCrafting1 = serializer("globe", () -> new SimpleCraftingRecipeSerializer<>(GlobeCraftingRecipe::new));
    public static RegistryObject<CreativeModeTab> globeItemGroup1 = TABS.register("globe_group", () -> CreativeModeTab.builder()
            .title(Component.literal("Globes"))
            .icon(() -> new ItemStack(globeBlockItem1.get()))
            .displayItems((context, entries) -> {
                for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                    if (block.defaultBlockState().is(CommonClass.BASE_BLOCK_TAG)) {
                        entries.accept(globeBlockItem1.get().getWithBase(block));
                    }
                }
                entries.accept(globeBlockItem1.get());
            })
            .build());

    //public static RegistryObject<Codec<VoidChunkGenerator>> VOID_CHUNK = CHUNK_GENERATOR.register("globe", () -> VoidChunkGenerator.CODEC);

    @SubscribeEvent
    public static void init(RegisterEvent event) {
        event.register(Registries.CHUNK_GENERATOR, helper -> {
            helper.register(globeID, VoidChunkGenerator.CODEC);
        });
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> blockEntity(String name, BlockEntityType.BlockEntitySupplier<T> tile, Supplier<Block[]> blocks) {
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(tile, blocks.get()).build(null));
    }

    public static <T extends CustomRecipe> RegistryObject<RecipeSerializer<T>> serializer(String name, Supplier<RecipeSerializer<T>> serializer) {
        return SERIALIZERS.register(name, serializer);
    }
}
