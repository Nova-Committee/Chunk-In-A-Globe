package committee.nova.mods.dg;

import committee.nova.mods.dg.common.item.FabricGlobeBlockItem;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import committee.nova.mods.dg.common.crafting.GlobeCraftingRecipe;
import committee.nova.mods.dg.events.BaseEventHandler;
import committee.nova.mods.dg.common.block.GlobeBlock;
import committee.nova.mods.dg.common.tile.GlobeBlockEntity;
import committee.nova.mods.dg.common.world.VoidChunkGenerator;
import committee.nova.mods.dg.common.net.FabricGlobeSectionManagerServer;
import committee.nova.mods.dg.common.net.pkt.FabricUpdateRequestPkt;
import committee.nova.mods.dg.utils.FabricDimensionHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.block.Block;

import static committee.nova.mods.dg.CommonClass.*;

public class DimensionGlobeFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		//CommonClass.init();
		globeBlock = new GlobeBlock();
		globeBlockItem = new FabricGlobeBlockItem(globeBlock, new Item.Properties());
		globeCrafting = new SimpleCraftingRecipeSerializer<>(GlobeCraftingRecipe::new);
		globeBlockEntityType = FabricBlockEntityTypeBuilder.create(GlobeBlockEntity::new, globeBlock).build(null);
		globeItemGroup = FabricItemGroup.builder()
				.title(Component.literal("Globes"))
				.icon(() -> new ItemStack(globeBlockItem))
				.displayItems((context, entries) -> {
					for (Block block : BuiltInRegistries.BLOCK.stream().toList()) {
						if (block.defaultBlockState().is(CommonClass.BASE_BLOCK_TAG)) {
							entries.accept(CommonClass.globeBlockItem.getWithBase(block));
						}
					}
					entries.accept(globeBlockItem);
				})
				.build();
		globeBlockItem.registerBlocks(Item.BY_BLOCK, globeBlockItem);

		managerServer = new FabricGlobeSectionManagerServer();
		dimensionHelper = new FabricDimensionHelper();

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, globeID, globeItemGroup);
		Registry.register(BuiltInRegistries.BLOCK, globeID, globeBlock);
		Registry.register(BuiltInRegistries.ITEM, globeID, globeBlockItem);
		Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, globeID, CommonClass.globeBlockEntityType);
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, globeID, VoidChunkGenerator.CODEC);
		Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Constants.rl("globe_crafting"), globeCrafting);


		ServerTickEvents.START_WORLD_TICK.register(BaseEventHandler::onWorldLTickPre);
		AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos, direction) -> BaseEventHandler.onLeftClickBlock(world, blockPos ));
		UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> BaseEventHandler.onRightClickBlock(world, playerEntity, hand));

		ServerPlayNetworking.registerGlobalReceiver(Constants.rl( "update_request"), (server, player, handler, packetByteBuf, responseSender) -> {
			final int amount = packetByteBuf.readInt();
			IntSet updateQueue = new IntOpenHashSet();
			for (int i = 0; i < amount; i++) {
				updateQueue.add(packetByteBuf.readInt());
			}
			FabricUpdateRequestPkt.run(server, updateQueue, player);
		});
	}
}
