package committee.nova.mods.dg.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class VoidChunkGenerator extends ChunkGenerator {

	public static final Codec<VoidChunkGenerator> CODEC =  RecordCodecBuilder.create(instance ->
			instance.group(RegistryOps.retrieveElement(Biomes.PLAINS)).apply(instance, instance.stable(VoidChunkGenerator::new)));

	 public VoidChunkGenerator(Holder<Biome> biomeEntry) {
        super(new FixedBiomeSource(biomeEntry));
    }


	@Override
	protected @NotNull Codec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public void applyCarvers(@NotNull WorldGenRegion worldGenRegion, long l, @NotNull RandomState randomState, @NotNull BiomeManager biomeManager, @NotNull StructureManager structureManager, @NotNull ChunkAccess chunkAccess, GenerationStep.@NotNull Carving carving) {

	}

	@Override
	public void buildSurface(@NotNull WorldGenRegion worldGenRegion, @NotNull StructureManager structureManager, @NotNull RandomState randomState, @NotNull ChunkAccess chunkAccess) {

	}

	@Override
	public void spawnOriginalMobs(@NotNull WorldGenRegion worldGenRegion) {

	}

	@Override
	public int getGenDepth() {
		return 384;
	}

	@Override
	public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor executor, @NotNull Blender blender, @NotNull RandomState randomState, @NotNull StructureManager structureManager, @NotNull ChunkAccess chunkAccess) {
		return CompletableFuture.completedFuture(chunkAccess);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public int getBaseHeight(int i, int i1, Heightmap.@NotNull Types types, @NotNull LevelHeightAccessor levelHeightAccessor, @NotNull RandomState randomState) {
		return 0;
	}

	@Override
	public @NotNull NoiseColumn getBaseColumn(int i, int i1, @NotNull LevelHeightAccessor levelHeightAccessor, @NotNull RandomState randomState) {
		return new NoiseColumn(0, new BlockState[0]);
	}

	@Override
	public void addDebugScreenInfo(@NotNull List<String> list, @NotNull RandomState randomState, @NotNull BlockPos blockPos) {
	}
}