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
            RecordCodecBuilder.mapCodec(yes -> yes.group(
                    EpicBiomeSource.CODEC.fieldOf("biome_source").forGetter(EpicChunkGenerator::getBiomeSource)
            ).apply(yes, yes.stable(EpicChunkGenerator::new)));

    private final EpicBiomeSource source;
    private final List<BlockState> states;

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
    protected @NotNull MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(@NotNull WorldGenRegion pLevel, long pSeed, @NotNull RandomState pRandom, @NotNull BiomeManager pBiomeManager, @NotNull StructureManager pStructureManager, @NotNull ChunkAccess pChunk, GenerationStep.@NotNull Carving pStep) {

    }

    @Override
    public void buildSurface(@NotNull WorldGenRegion pLevel, @NotNull StructureManager pStructureManager, @NotNull RandomState pRandom, @NotNull ChunkAccess pChunk) {

    }

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion pLevel) {

    }

    @Override
    public int getGenDepth() {
        return 384;
    }

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
