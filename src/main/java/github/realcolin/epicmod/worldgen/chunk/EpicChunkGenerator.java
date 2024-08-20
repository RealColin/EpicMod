package github.realcolin.epicmod.worldgen.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import github.realcolin.epicmod.worldgen.biome.EpicBiomeSource;
import github.realcolin.epicmod.worldgen.map.MapImage;

import github.realcolin.epicmod.worldgen.noise.Perlin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EpicChunkGenerator extends ChunkGenerator {

    public static final MapCodec<EpicChunkGenerator> CODEC =
            RecordCodecBuilder.mapCodec(map -> map.group(
                    MapImage.CODEC.fieldOf("map").forGetter(src -> src.map)
            ).apply(map, map.stable(EpicChunkGenerator::new)));

    private final MapImage map;

    private final List<BlockState> states; // TODO get rid of this

    public EpicChunkGenerator(MapImage map) {
        super(map.getSource());
        this.map = map;

        // TODO get rid of all below
        this.states = new ArrayList<>();

        states.add(Blocks.BEDROCK.defaultBlockState());
        for (int i = 0; i < 64; i++) {
            states.add(Blocks.STONE.defaultBlockState());
        }
        states.add(Blocks.GRASS_BLOCK.defaultBlockState());
    }

    @Override
    public @NotNull EpicBiomeSource getBiomeSource() {
        return map.getSource();
    }

    @Override
    protected @NotNull MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(@NotNull WorldGenRegion pLevel, long pSeed, @NotNull RandomState pRandom, @NotNull BiomeManager pBiomeManager, @NotNull StructureManager pStructureManager, @NotNull ChunkAccess pChunk, GenerationStep.@NotNull Carving pStep) {

    }

    @Override
    public void buildSurface(@NotNull WorldGenRegion pLevel, @NotNull StructureManager pStructureManager, @NotNull RandomState pRandom, @NotNull ChunkAccess pChunk) {


        //        if (noise == null)
//            noise = new Perlin(pLevel.getSeed());
//
//        int minHeight = pChunk.getMinBuildHeight();
//
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                int posX = pChunk.getPos().x * 16 + x;
//                int posZ = pChunk.getPos().z * 16 + z;
//
//                // type of terrain at the position based on the color
//                TerrainType type;
//
//                // get height somehow from the terrain type, (x, z) coordinate, and
//                int height;
//
//
//            }
//        }
    }

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion pLevel) {

    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    // TODO empty all of this
    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor pExecutor, @NotNull Blender pBlender, @NotNull RandomState pRandom, @NotNull StructureManager pStructureManager, ChunkAccess pChunk) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        Heightmap heightmap = pChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = pChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

        for (int i = 0; i < Math.min(pChunk.getHeight(), states.size()); i++) {
            BlockState blockstate = states.get(i);
            if (blockstate != null) {
                int j = pChunk.getMinBuildHeight() + i;

                for (int k = 0; k < 16; k++) {
                    for (int l = 0; l < 16; l++) {
                        pChunk.setBlockState(blockpos$mutableblockpos.set(k, j, l), blockstate, false);
                        heightmap.update(k, j, l, blockstate);
                        heightmap1.update(k, j, l, blockstate);
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(pChunk);
    }

    @Override
    public int getSeaLevel() {
        return -63;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    // TODO empty this
    @Override
    public int getBaseHeight(int pX, int pZ, Heightmap.@NotNull Types pType, LevelHeightAccessor pLevel, @NotNull RandomState pRandom) {

        for (int i = Math.min(states.size(), pLevel.getMaxBuildHeight()) - 1; i >= 0; i--) {
            BlockState state = states.get(i);
            if (state != null && pType.isOpaque().test(state)) {
                return pLevel.getMinBuildHeight() + i + 1;
            }
        }

        return pLevel.getMinBuildHeight();
    }


    // TODO empty this
    @Override
    public @NotNull NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, @NotNull RandomState pRandom) {
        //BlockState[] blocks = new BlockState[]{Blocks.STONE.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()};

        return new NoiseColumn(
                pHeight.getMinBuildHeight(),
                states
                        .stream()
                        .limit(pHeight.getHeight())
                        .map(yea -> yea == null ? Blocks.AIR.defaultBlockState() : yea)
                        .toArray(BlockState[]::new));
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> pInfo, @NotNull RandomState pRandom, @NotNull BlockPos pPos) {

    }

    public static void registerChunkGenerator() {
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, new ResourceLocation(EpicMod.MOD_ID, "epic_generator"), CODEC);
    }
}
