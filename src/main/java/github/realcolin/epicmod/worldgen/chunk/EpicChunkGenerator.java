package github.realcolin.epicmod.worldgen.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import github.realcolin.epicmod.EpicMod;
import github.realcolin.epicmod.worldgen.biome.EpicBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EpicChunkGenerator extends ChunkGenerator {

    public static final MapCodec<EpicChunkGenerator> CODEC =
            RecordCodecBuilder.mapCodec(yes -> yes.group(
                    EpicBiomeSource.CODEC.fieldOf("biome_source").forGetter(EpicChunkGenerator::getBiomeSource)
            ).apply(yes, yes.stable(EpicChunkGenerator::new)));

    private final EpicBiomeSource source;
    private List<BlockState> states;

    public EpicChunkGenerator(EpicBiomeSource pBiomeSource) {
        super(pBiomeSource);
        this.source = pBiomeSource;

        this.states = new ArrayList<>();

        states.add(Blocks.BEDROCK.defaultBlockState());
        for (int i = 0; i < 64; i++) {
            states.add(Blocks.STONE.defaultBlockState());
        }
        states.add(Blocks.GRASS_BLOCK.defaultBlockState());
    }

    @Override
    public @NotNull EpicBiomeSource getBiomeSource() {
        return source;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(WorldGenRegion pLevel, long pSeed, RandomState pRandom, BiomeManager pBiomeManager, StructureManager pStructureManager, ChunkAccess pChunk, GenerationStep.Carving pStep) {

    }

    @Override
    public void buildSurface(WorldGenRegion pLevel, StructureManager pStructureManager, RandomState pRandom, ChunkAccess pChunk) {

    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion pLevel) {

    }

    @Override
    public int getGenDepth() {
        return 384;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor pExecutor, Blender pBlender, RandomState pRandom, StructureManager pStructureManager, ChunkAccess pChunk) {
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

    @Override
    public int getBaseHeight(int pX, int pZ, Heightmap.Types pType, LevelHeightAccessor pLevel, RandomState pRandom) {

        for (int i = Math.min(states.size(), pLevel.getMaxBuildHeight()) - 1; i >= 0; i--) {
            BlockState state = states.get(i);
            if (state != null && pType.isOpaque().test(state)) {
                return pLevel.getMinBuildHeight() + i + 1;
            }
        }

        return pLevel.getMinBuildHeight();
    }


    @Override
    public NoiseColumn getBaseColumn(int pX, int pZ, LevelHeightAccessor pHeight, RandomState pRandom) {
        //BlockState[] blocks = new BlockState[]{Blocks.STONE.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState()};

        return new NoiseColumn(
                pHeight.getMinBuildHeight(),
                states
                        .stream()
                        .limit((long)pHeight.getHeight())
                        .map(yea -> yea == null ? Blocks.AIR.defaultBlockState() : yea)
                        .toArray(BlockState[]::new));
    }

    @Override
    public void addDebugScreenInfo(List<String> pInfo, RandomState pRandom, BlockPos pPos) {

    }

    public static void registerChunkGenerator() {
        Registry.register(BuiltInRegistries.CHUNK_GENERATOR, new ResourceLocation(EpicMod.MOD_ID, "epic_generator"), CODEC);
    }
}
