package committee.nova.mods.dg.utils;

import committee.nova.mods.dg.CommonClass;
import committee.nova.mods.dg.Constants;
import committee.nova.mods.dg.globe.GlobeBlockEntity;
import it.unimi.dsi.fastutil.ints.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class GlobeManager extends SavedData {

	private static final String SAVE_KEY = Constants.MOD_ID + "_globes";
	private static long KEEP_ALIVE_TIME = 20 * 10; // 10 seconds

	public static GlobeManager getInstance(ServerLevel world) {
		if (world.dimension() != Level.OVERWORLD) {
			world = world.getServer().getLevel(Level.OVERWORLD);
		}
		final ServerLevel serverWorld = world;
		return serverWorld.getDataStorage()
				.computeIfAbsent((CompoundTag tag) -> GlobeManager.fromNbt(new GlobeManager(serverWorld), tag), () -> new GlobeManager(serverWorld), SAVE_KEY);
	}

	private final Int2ObjectMap<Globe> globes = new Int2ObjectArrayMap<>();
	private final Int2LongMap tickingGlobes = new Int2LongArrayMap();

	private final ServerLevel world;

	public GlobeManager(ServerLevel world) {
		super();
		this.world = world;
	}

	public Globe getNextGlobe() {
		final Globe globe = new Globe(globes.size());
		globes.put(globe.id, globe);
		return globe;
	}

	public Globe getGlobeByID(int id) {
		if (!globes.containsKey(id)) {
			throw new RuntimeException("Could not find globe with id: " + id);
		}
		return globes.get(id);
	}

	@SuppressWarnings("deprecation")
	public void tick() {
		final long currentTime = world.getGameTime();
		final IntList destoryQueue = new IntArrayList();
		for (Int2LongMap.Entry entry : tickingGlobes.int2LongEntrySet()) {
			if (currentTime - entry.getLongValue() > KEEP_ALIVE_TIME) {
				destoryQueue.add(entry.getIntKey());
			}
		}
		for (Integer forRemoval : destoryQueue) {
			unloadGlobe(forRemoval);
			tickingGlobes.remove(forRemoval);
		}
	}

	public void markGlobeForTicking(int id) {
		if (!tickingGlobes.containsKey(id)) {
			chunkLoadGlobe(id);
		}
		tickingGlobes.put(id, world.getGameTime());
	}

	private void chunkLoadGlobe(int id) {
		ChunkPos chunk = getGlobeByID(id).getChunkPos();
		getGlobeWorld().setChunkForced(chunk.x, chunk.z, true);
	}

	private void unloadGlobe(int id) {
		ChunkPos chunk = getGlobeByID(id).getChunkPos();
		getGlobeWorld().setChunkForced(chunk.x, chunk.z, false);
	}

	@Override
	public boolean isDirty() {
		//Always save please
		return true;
	}

	private ServerLevel getGlobeWorld() {
		return world.getServer().getLevel(CommonClass.globeDimension);
	}

	public static GlobeManager fromNbt(GlobeManager manager, CompoundTag tag) {
		manager.globes.clear();
		CompoundTag globesTag = tag.getCompound("globes");
		for (String key : globesTag.getAllKeys()) {
			int keyID = Integer.parseInt(key);
			manager.globes.put(keyID, new Globe(keyID, globesTag.getCompound(key)));
		}

		CompoundTag tickingGlobesTag = tag.getCompound("ticking_globes");
		for (String key : tickingGlobesTag.getAllKeys()) {
			int keyID = Integer.parseInt(key);
			manager.tickingGlobes.put(keyID, manager.world.getGameTime());
		}
		return manager;
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		CompoundTag globesTag = new CompoundTag();
		for (Int2ObjectMap.Entry<Globe> entry : globes.int2ObjectEntrySet()) {
			globesTag.put(entry.getIntKey() + "", entry.getValue().toTag());
		}
		tag.put("globes", globesTag);

		CompoundTag tickingGlobesTag = new CompoundTag();
		for (Int2LongMap.Entry entry : tickingGlobes.int2LongEntrySet()) {
			tickingGlobesTag.putBoolean(entry.getIntKey() + "", true);
		}
		tag.put("ticking_globes", tickingGlobesTag);
		return tag;
	}

	public static class Globe {
		private final int id;
		private GlobeSection globeSection = null;
		private GlobeSection innerGlobeSection = null;

		public Globe(int id) {
			this.id = id;
		}

		public Globe(int id, CompoundTag compoundTag) {
			this(id);
			fromTag(compoundTag);
		}

		public ChunkPos getChunkPos() {
			return new ChunkPos(0, id * 100);
		}

		public BlockPos getGlobeLocation() {
			BlockPos chunkPos = getChunkPos().getWorldPosition();
			return new BlockPos(chunkPos.getX(), 128, chunkPos.getZ());
		}

		public void fromTag(CompoundTag tag) {

		}

		public CompoundTag toTag() {
			return new CompoundTag();
		}

		public void updateBlockSection(ServerLevel world, boolean inner, GlobeBlockEntity blockEntity) {
			if (inner) {
				if (innerGlobeSection == null) {
					innerGlobeSection = new GlobeSection();
				}
				innerGlobeSection.buildBlockMap(world, blockEntity.getInnerScanPos());
			} else {
				if (globeSection == null) {
					globeSection = new GlobeSection();
				}
				globeSection.buildBlockMap(world, getGlobeLocation());
			}

		}

		public void updateEntitySection(ServerLevel world, boolean inner, GlobeBlockEntity blockEntity) {
			if (inner) {
				if (innerGlobeSection == null) {
					innerGlobeSection = new GlobeSection();
				}
				innerGlobeSection.buildEntityList(world, blockEntity.getInnerScanPos());
			} else {
				if (globeSection == null) {
					globeSection = new GlobeSection();
				}
				globeSection.buildEntityList(world, getGlobeLocation());
			}

		}

		public GlobeSection getGlobeSection(boolean inner) {
			if (inner) {
				return innerGlobeSection;
			}
			return globeSection;
		}

		public int getId() {
			return id;
		}
	}
}
