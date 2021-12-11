package com.sonicether.soundphysics;

import com.sonicether.soundphysics.config.ConfigManager;
import com.sonicether.soundphysics.config.PrecomputedConfig;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import static com.sonicether.soundphysics.RaycastFix.fixedRaycast;
import static com.sonicether.soundphysics.SPLog.*;
import static com.sonicether.soundphysics.SPEfx.*;
import static java.util.Map.entry;

@SuppressWarnings({"NonAsciiCharacters", "CommentedOutCode"})
public class SoundPhysics
{

	private static final Pattern rainPattern = Pattern.compile(".*rain.*");
	public static final Pattern stepPattern = Pattern.compile(".*step.*");
	private static final Pattern blockPattern = Pattern.compile(".*block..*");
	private static final Pattern uiPattern = Pattern.compile("ui..*");
	public static final Map<BlockSoundGroup, BlockSoundGroup> redirectMap = /*<editor-fold desc="Map.ofEntries()">*/Map.ofEntries(
			entry(BlockSoundGroup.MOSS_CARPET, BlockSoundGroup.MOSS_BLOCK),			// first becomes second
			entry(BlockSoundGroup.AMETHYST_CLUSTER, BlockSoundGroup.AMETHYST_BLOCK),
			entry(BlockSoundGroup.SMALL_AMETHYST_BUD, BlockSoundGroup.AMETHYST_BLOCK),
			entry(BlockSoundGroup.MEDIUM_AMETHYST_BUD, BlockSoundGroup.AMETHYST_BLOCK),
			entry(BlockSoundGroup.LARGE_AMETHYST_BUD, BlockSoundGroup.AMETHYST_BLOCK),
			entry(BlockSoundGroup.POINTED_DRIPSTONE, BlockSoundGroup.DRIPSTONE_BLOCK),
			entry(BlockSoundGroup.FLOWERING_AZALEA, BlockSoundGroup.AZALEA),
			entry(BlockSoundGroup.DEEPSLATE_BRICKS, BlockSoundGroup.POLISHED_DEEPSLATE),
			entry(BlockSoundGroup.COPPER, BlockSoundGroup.METAL),
			entry(BlockSoundGroup.ANVIL, BlockSoundGroup.METAL),
			entry(BlockSoundGroup.NETHER_SPROUTS, BlockSoundGroup.ROOTS),
			entry(BlockSoundGroup.WEEPING_VINES_LOW_PITCH, BlockSoundGroup.WEEPING_VINES),
			entry(BlockSoundGroup.LILY_PAD, BlockSoundGroup.WET_GRASS),
			entry(BlockSoundGroup.NETHER_GOLD_ORE, BlockSoundGroup.NETHERRACK),
			entry(BlockSoundGroup.NETHER_ORE, BlockSoundGroup.NETHERRACK),
			entry(BlockSoundGroup.CALCITE, BlockSoundGroup.STONE),
			entry(BlockSoundGroup.GILDED_BLACKSTONE, BlockSoundGroup.STONE),
			entry(BlockSoundGroup.SMALL_DRIPLEAF, BlockSoundGroup.CAVE_VINES),
			entry(BlockSoundGroup.BIG_DRIPLEAF, BlockSoundGroup.CAVE_VINES),
			entry(BlockSoundGroup.SPORE_BLOSSOM, BlockSoundGroup.CAVE_VINES),
			entry(BlockSoundGroup.GLOW_LICHEN, BlockSoundGroup.VINE),
			entry(BlockSoundGroup.HANGING_ROOTS, BlockSoundGroup.VINE),
			entry(BlockSoundGroup.ROOTED_DIRT, BlockSoundGroup.GRAVEL),
			entry(BlockSoundGroup.WART_BLOCK, BlockSoundGroup.NETHER_WART),
			entry(BlockSoundGroup.CROP, BlockSoundGroup.GRASS),
			entry(BlockSoundGroup.BAMBOO_SAPLING, BlockSoundGroup.GRASS),
			entry(BlockSoundGroup.SWEET_BERRY_BUSH, BlockSoundGroup.GRASS),
			entry(BlockSoundGroup.SCAFFOLDING, BlockSoundGroup.BAMBOO),
			entry(BlockSoundGroup.LODESTONE, BlockSoundGroup.NETHERITE),
			entry(BlockSoundGroup.LADDER, BlockSoundGroup.WOOD)
	)/*</editor-fold>*/;
	public static PrecomputedConfig pC;
	//Private fields
	// ψ time ψ
	//public static double tt = 0;
	//private static long ttt;
	//private static double cumtt = 0;
	//private static long navgt = 0;
	//public static void t1() {ttt = System.nanoTime(); }
	//public static void t2() { SoundPhysics.tt += (System.nanoTime()-ttt)/1000000d;}
	//public static void tavg() { cumtt += tt; navgt++; }
	//public static void tout() { System.out.println(String.valueOf(SoundPhysics.tt) + "   Avg: " + String.valueOf(cumtt/navgt)); }
	//public static void tres() { SoundPhysics.tt = 0; }
	private static MinecraftClient mc;
	
	private static SoundCategory lastSoundCategory;
	private static String lastSoundName;

	//Public fields
	public final static float globalVolumeMultiplier = 4f;

	public static void init()
	{
		log("Initializing Sound Physics...");
		setupEFX();
		log("EFX ready...");
		mc = MinecraftClient.getInstance();
		//rand = new Random(System.currentTimeMillis());
	}

	public static void setLastSoundCategoryAndName(SoundCategory sc, String name)
	{//log("Set last sound category and name");
		lastSoundCategory = sc;
		lastSoundName = name;
	}
	
	public static void onPlaySound(double posX, double posY, double posZ, int sourceID)
	{
		if (pC.dLog) logGeneral("On play sound... Source ID: " + sourceID + " " + posX + ", " + posY + ", " + posZ + "    Sound category: " + lastSoundCategory.toString() + "    Sound name: " + lastSoundName);

		long startTime = 0;
		long endTime;
		
		if (pC.pLog) startTime = System.nanoTime();

		evaluateEnvironment(sourceID, posX, posY, posZ);
		
		if (pC.pLog)
		{ endTime = System.nanoTime();
			log("Total calculation time for sound " + lastSoundName + ": " + (double)(endTime - startTime)/(double)1000000 + " milliseconds"); }

	}
	
	private static double getBlockReflectivity(final BlockPos blockPos)
	{
		assert mc.world != null;
		BlockState blockState = mc.world.getBlockState(blockPos);
		BlockSoundGroup soundType = blockState.getSoundGroup();

		double r = pC.reflectivityMap.getOrDefault(soundType, Double.NaN);
		if (Double.isNaN(r)) {
			String blockName = blockState.getBlock().getTranslationKey();
			if (pC.blockWhiteSet.contains(blockName)) return pC.blockWhiteMap.get(blockName).reflectivity;
			else return pC.defaultReflectivity;
		}
		return r;
	}

	private static double getBlockOcclusionD(final BlockPos blockPos)
	{
		assert mc.world != null;
		BlockState blockState = mc.world.getBlockState(blockPos);
		BlockSoundGroup soundType = blockState.getSoundGroup();

		double r = pC.absorptionMap.getOrDefault(soundType, Double.NaN);
		if (Double.isNaN(r)) {
			String blockName = blockState.getBlock().getTranslationKey();
			if (pC.blockWhiteSet.contains(blockName)) return pC.blockWhiteMap.get(blockName).absorption;
			else return pC.defaultAbsorption;
		}
		return r;
	}

	private static Vec3d pseudoReflect(Vec3d dir, Vec3i normal)
	{return new Vec3d(normal.getX() == 0 ? dir.x : -dir.x, normal.getY() == 0 ? dir.y : -dir.y, normal.getZ() == 0 ? dir.z : -dir.z);}

	@SuppressWarnings("ConstantConditions")
	private static void evaluateEnvironment(final int sourceID, final double posX, final double posY, final double posZ)
	{
		if (!pC.on) return;
		if (mc.player == null || mc.world == null || posY <= mc.world.getBottomY() || lastSoundCategory == SoundCategory.RECORDS || uiPattern.matcher(lastSoundName).matches() || (posX == 0.0 && posY == 0.0 && posZ == 0.0))
		{
			//logDetailed("Menu sound!");
			
			setEnvironment(sourceID, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}

		final boolean isRain = rainPattern.matcher(lastSoundName).matches();

		if (pC.skipRainOcclusionTracing && isRain)
		{
			setEnvironment(sourceID, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		final long timeT = mc.world.getTime();
		if (RaycastFix.lastUpd != timeT) {
			//tavg();tout();tres();// ψ time ψ
			if (timeT % 1024 == 0) {
				RaycastFix.shapeCache = new Long2ObjectOpenHashMap<>(2048,0.75f); // just in case something gets corrupted
				//cumtt = 0; navgt = 0;
			}
			else {
				RaycastFix.shapeCache.clear();
			}
			RaycastFix.lastUpd = timeT;
		}

		final int numRays = pC.environmentEvaluationRays;
		final int rayBounces = pC.environmentEvaluationRayBounces;
		final double rcpTotalRays = 1.0f / (numRays * rayBounces);
		final double rcpPrimaryRays = 1.0f / (numRays);

		double directCutoff;
		final double absorptionCoeff = pC.globalBlockAbsorption * 3.0;
		
		//Direct sound occlusion
		Vec3d playerPos = mc.player.getPos();
			  playerPos = new Vec3d(playerPos.x, playerPos.y + mc.player.getEyeHeight(mc.player.getPose()), playerPos.z);
		final Vec3d soundPos = new Vec3d(posX, posY, posZ);
		Vec3d normalToPlayer = playerPos.subtract(soundPos).normalize();

		final BlockPos soundBlockPos = new BlockPos(soundPos.x, soundPos.y,soundPos.z);

		if (pC.dLog) logGeneral("Player pos: " + playerPos.x + ", " + playerPos.y + ", " + playerPos.z + "      Sound Pos: " + soundPos.x + ", " + soundPos.y + ", " + soundPos.z + "       To player vector: " + normalToPlayer.x + ", " + normalToPlayer.y + ", " + normalToPlayer.z);
		double occlusionAccumulation = 0.0f;
		boolean doDirEval = pC.soundDirectionEvaluation;
		final List<Map.Entry<Vec3d, Double>> directions = new Vector<>(10, 10);
		//Cast a ray from the source towards the player
		Vec3d rayOrigin = soundPos;
		//System.out.println(rayOrigin.toString());
		BlockPos lastBlockPos = soundBlockPos;
		final boolean _9ray = pC._9Ray && (lastSoundCategory == SoundCategory.BLOCKS || blockPattern.matcher(lastSoundName).matches()) && !stepPattern.matcher(lastSoundName).matches();
		final int nOccRays = _9ray ? 9 : 1;
		double occlusionAccMin = Double.MAX_VALUE;
		for (int j = 0; j < nOccRays; j++) {
			if(j > 0){
				final int jj = j - 1;
				rayOrigin = new Vec3d(soundBlockPos.getX() + 0.001 + 0.998 * (jj % 2), soundBlockPos.getY() + 0.001 + 0.998 * ((jj >> 1) % 2), soundBlockPos.getZ() + 0.001 + 0.998 * ((jj >> 2) % 2));
				lastBlockPos = soundBlockPos;
				occlusionAccumulation = 0.0f;
			}

			BlockHitResult rayHit = fixedRaycast(new RaycastContext(rayOrigin, playerPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, lastBlockPos);

			for (int i = 0; i < 10; i++) {

				lastBlockPos = rayHit.getBlockPos();
				//If we hit a block

				RaycastRenderer.addOcclusionRay(rayOrigin, rayHit.getPos(), Color.getHSBColor((float) (1F / 3F * (1F - Math.min(1F, occlusionAccumulation / 12F))), 1F, 1F).getRGB());
				if (rayHit.getType() == HitResult.Type.MISS) {
					if (doDirEval) directions.add(Map.entry(rayOrigin.subtract(playerPos),  (_9ray?9.0:1.0)*Math.pow(soundPos.distanceTo(playerPos) ,2)/(Math.exp(-occlusionAccumulation * absorptionCoeff)*Math.pow(pC.directRaysDirEvalMultiplier, 10.66))*rcpTotalRays));
					break;
				}

				final BlockPos blockHitPos = rayHit.getBlockPos();
				final Vec3d rayHitPos = rayHit.getPos();
				final BlockState blockHit = mc.world.getBlockState(blockHitPos);
				double blockOcclusion = getBlockOcclusionD(blockHitPos);//todo add blockstate to blockhitresult

				// Regardless to whether we hit from inside or outside

				if (pC.oLog) logOcclusion(blockHit.getBlock().getTranslationKey() + "    " + rayHitPos.x + ", " + rayHitPos.y + ", " + rayHitPos.z);

				rayOrigin = rayHitPos; //new Vec3d(rayHit.getPos().x + normalToPlayer.x * 0.1, rayHit.getPos().y + normalToPlayer.y * 0.1, rayHit.getPos().z + normalToPlayer.z * 0.1);

				rayHit = fixedRaycast(new RaycastContext(rayOrigin, playerPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, lastBlockPos);

				BlockHitResult rayBack = fixedRaycast(new RaycastContext(rayHit.getPos(), rayOrigin, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, rayHit.getBlockPos());

				if (!rayBack.getBlockPos().equals(lastBlockPos)) {
					logError("[Occlusion reverse]Block "+lastBlockPos.toString()+ " is not "+rayBack.getBlockPos().toString() );
					occlusionAccumulation += blockOcclusion;
				}
				else
				{
					//Accumulate density
					occlusionAccumulation += blockOcclusion * (rayOrigin.distanceTo(rayBack.getPos()));
				}



				if (pC.oLog) logOcclusion("New trace position: " + rayOrigin.x + ", " + rayOrigin.y + ", " + rayOrigin.z);
			}
			occlusionAccMin = Math.min(occlusionAccMin, occlusionAccumulation);
		}
		occlusionAccumulation = Math.min(occlusionAccMin, pC.maxDirectOcclusionFromBlocks);
		directCutoff = Math.exp(-occlusionAccumulation * absorptionCoeff);
		double directGain = Math.pow(directCutoff, 0.1);

		if (pC.oLog) logOcclusion("direct cutoff: " + directCutoff + "  direct gain:" + directGain);

		final double[] δsendGain = {0,0,0,0};

		if (isRain) {finalizeEnvironment(true, sourceID, directCutoff, 0, 0, 0, occlusionAccumulation, absorptionCoeff, directGain, null, δsendGain); return;}

		// Shoot rays around sound

		final double maxDistance = 256.0f * pC.environmentEvaluationRayBounces;

		doDirEval = pC.soundDirectionEvaluation &&
				(occlusionAccumulation > 0 || !pC.notOccludedNoRedirect);

		final double[] bounceReflectivityRatio = new double[rayBounces];
		
		double sharedAirspace = 0.0f;

		final double gRatio = 1.618033988f;
		final double epsilon;
		
		if (numRays >= 600000)
		{
			epsilon = 214.0f;
		}
		else if (numRays >= 400000)
		{
			epsilon = 75.0f;
		}
		else if (numRays >= 11000)
		{
			epsilon = 27.0f;
		}
		else if (numRays >= 890)
		{
			epsilon = 10.0f;
		}
		else if (numRays >= 177)
		{
			epsilon = 3.33f;
		}
		else if (numRays >= 24)
		{
			epsilon = 1.33f;
		}
		else
		{
			epsilon = 0.33f;
		}

		for (int i = 0; i < numRays; i++)
		{
			final double x = (i + epsilon) / (numRays - 1.0 + 2.0*epsilon);
			final double y = (double) i / gRatio;
			final double theta = 2.0f * Math.PI * y;
			final double phi = Math.acos(1.0f - 2.0f*x);
			
			final Vec3d rayDir = new Vec3d(Math.cos(theta) * Math.sin(phi),
					Math.sin(theta) * Math.sin(phi), Math.cos(phi));

			final Vec3d rayEnd = new Vec3d(soundPos.x + rayDir.x * maxDistance, soundPos.y + rayDir.y * maxDistance,
					soundPos.z + rayDir.z * maxDistance);

			BlockHitResult rayHit = fixedRaycast(new RaycastContext(soundPos, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, soundBlockPos);

			RaycastRenderer.addSoundBounceRay(soundPos, rayHit.getPos(), Formatting.GREEN.getColorValue());

			if (rayHit.getType() == HitResult.Type.BLOCK) {
				
				// Additional bounces
				BlockPos lastHitBlock = rayHit.getBlockPos();
				Vec3d lastHitPos = rayHit.getPos();
				Vec3i lastHitNormal = rayHit.getSide().getVector();
				Vec3d lastRayDir = rayDir;
				
				double totalRayDistance = soundPos.distanceTo(rayHit.getPos());

				double blockReflectivity = getBlockReflectivity(lastHitBlock);

				double totalReflectivityCoefficient = Math.min(blockReflectivity, 1);
				
				// Secondary ray bounces
				for (int j = 0; j < rayBounces; j++) {
					// Cast (one) final ray towards the player. If it's
					// unobstructed, then the sound source and the player
					// share airspace.
					final double energyTowardsPlayer = 0.25f * (blockReflectivity * pC.globalBlockReflectance * 0.75f + 0.25f);
					if (!pC.simplerSharedAirspaceSimulation || j == rayBounces - 1) {
						final Vec3d finalRayStart = new Vec3d(lastHitPos.x + lastHitNormal.getX() * 0.01,
								lastHitPos.y + lastHitNormal.getY() * 0.01, lastHitPos.z + lastHitNormal.getZ() * 0.01);

						final BlockHitResult finalRayHit = fixedRaycast(new RaycastContext(finalRayStart, playerPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, null);

						int color = Formatting.GRAY.getColorValue();
						if (finalRayHit.getType() == HitResult.Type.MISS) {
							color = Formatting.WHITE.getColorValue();

							double totalFinalRayDistance = totalRayDistance + finalRayStart.distanceTo(playerPos);

							if (doDirEval) directions.add(Map.entry(finalRayStart.subtract(playerPos), (totalFinalRayDistance*totalFinalRayDistance)*(totalReflectivityCoefficient == 0 ? 1000000 : 1/totalReflectivityCoefficient)));
							//log("Secondary ray hit the player!");

							sharedAirspace += 1.0f;

							final double reflectionDelay = Math.max(totalRayDistance, 0.0) * 0.12 * blockReflectivity * pC.globalBlockReflectance;

							final double cross0 = 1.0f - MathHelper.clamp(Math.abs(reflectionDelay - 0.0f), 0.0f, 1.0f);
							final double cross1 = 1.0f - MathHelper.clamp(Math.abs(reflectionDelay - 1.0f), 0.0f, 1.0f);
							final double cross2 = 1.0f - MathHelper.clamp(Math.abs(reflectionDelay - 2.0f), 0.0f, 1.0f);
							final double cross3 = MathHelper.clamp(reflectionDelay - 2.0f, 0.0f, 1.0f);

							δsendGain[0] += cross0 * energyTowardsPlayer * 6.4f * rcpTotalRays;
							δsendGain[1] += cross1 * energyTowardsPlayer * 12.8f * rcpTotalRays;
							δsendGain[2] += cross2 * energyTowardsPlayer * 12.8f * rcpTotalRays;
							δsendGain[3] += cross3 * energyTowardsPlayer * 12.8f * rcpTotalRays;

						}
						RaycastRenderer.addSoundBounceRay(finalRayStart, finalRayHit.getPos(), color);
					}

					final Vec3d newRayDir = pseudoReflect(lastRayDir, lastHitNormal);
					final Vec3d newRayStart = lastHitPos;
					final Vec3d newRayEnd = new Vec3d(newRayStart.x + newRayDir.x * (maxDistance - totalRayDistance),
							newRayStart.y + newRayDir.y * (maxDistance - totalRayDistance), newRayStart.z + newRayDir.z * (maxDistance - totalRayDistance));
					
					//log("New ray dir: " + newRayDir.xCoord + ", " + newRayDir.yCoord + ", " + newRayDir.zCoord);

					BlockHitResult newRayHit = fixedRaycast(new RaycastContext(newRayStart, newRayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, lastHitBlock);


					if (newRayHit.getType() == HitResult.Type.MISS) {
						RaycastRenderer.addSoundBounceRay(newRayStart, newRayEnd, Formatting.DARK_RED.getColorValue());
						break;
					} else {
						final Vec3d newRayHitPos = newRayHit.getPos();
						final double newRayLength = lastHitPos.distanceTo(newRayHitPos);

						RaycastRenderer.addSoundBounceRay(newRayStart, newRayHitPos, Formatting.BLUE.getColorValue());



						bounceReflectivityRatio[j] += blockReflectivity * pC.globalBlockReflectance;


						totalRayDistance += newRayLength;

						lastHitPos = newRayHitPos;
						lastHitNormal = newRayHit.getSide().getVector();
						lastRayDir = newRayDir;
						lastHitBlock = newRayHit.getBlockPos();
						blockReflectivity = getBlockReflectivity(lastHitBlock);
						totalReflectivityCoefficient *= Math.min(blockReflectivity, 1);

					}
				}
			}
		}
		for (int i = 0; i < bounceReflectivityRatio.length; i++) {
			bounceReflectivityRatio[i] = bounceReflectivityRatio[i] / numRays;
		}

		// Take weighted (on squared distance) average of the directions sound reflection came from
		dirEval:
		{
			if (directions.isEmpty()) break dirEval;
			if (pC.pLog) log("Evaluating direction from "+sharedAirspace+" entries...");
			Vec3d sum = new Vec3d(0, 0, 0);
			double weight = 0;

			for (Map.Entry<Vec3d, Double> direction : directions) {
				double val = direction.getValue();
				if ( val <= 0.0 ) break dirEval;
				final double w = 1 / val;
				weight += w;
				sum = sum.add(direction.getKey().normalize().multiply(w));
			}
			sum = sum.multiply(1 / weight);
			//System.out.println(sum+"  "+sum.lengthSquared());
			if (sum.lengthSquared() >= 1-pC.maxDirVariance)
				setSoundPos(sourceID, sum.normalize().multiply(soundPos.distanceTo(playerPos)).add(playerPos));
			// ψ this shows a star at perceived sound pos ψ
			// Vec3d pos = sum.normalize().multiply(soundPos.distanceTo(playerPos)).add(playerPos);
			// mc.world.addParticle(ParticleTypes.END_ROD, false, pos.getX(), pos.getY(), pos.getZ(), 0,0,0);
		}

		finalizeEnvironment(false, sourceID, directCutoff, sharedAirspace, rcpPrimaryRays, rcpTotalRays, occlusionAccumulation, absorptionCoeff, directGain, bounceReflectivityRatio, δsendGain);
		}

	private static void finalizeEnvironment(boolean isRain, int sourceID, double directCutoff, double sharedAirspace, double rcpPrimaryRays, double rcpTotalRays, double occlusionAccumulation, double absorptionCoeff, double directGain, double[] bounceReflectivityRatio, double[] δsendGain) {

		// Calculate reverb parameters for this sound
		double sendGain0 = 0.0f + δsendGain[0];
		double sendGain1 = 0.0f + δsendGain[1];
		double sendGain2 = 0.0f + δsendGain[2];
		double sendGain3 = 0.0f + δsendGain[3];

		double sendCutoff0 = 1.0f;
		double sendCutoff1 = 1.0f;
		double sendCutoff2 = 1.0f;
		double sendCutoff3 = 1.0f;

		assert mc.player != null;
		if (mc.player.isSubmergedInWater())
		{
			directCutoff *= 1.0f - pC.underwaterFilter;
		}

		if (isRain)
		{
			setEnvironment(sourceID, sendGain0, sendGain1, sendGain2, sendGain3, sendCutoff0, sendCutoff1, sendCutoff2, sendCutoff3, directCutoff, directGain);
			return;
		}

		sharedAirspace *= 64.0f;

		if (pC.simplerSharedAirspaceSimulation)
			sharedAirspace *= rcpPrimaryRays;
		else
			sharedAirspace *= rcpTotalRays;

		final double sharedAirspaceWeight0 = MathHelper.clamp(sharedAirspace / 20.0f, 0.0f, 1.0f);
		final double sharedAirspaceWeight1 = MathHelper.clamp(sharedAirspace / 15.0f, 0.0f, 1.0f);
		final double sharedAirspaceWeight2 = MathHelper.clamp(sharedAirspace / 10.0f, 0.0f, 1.0f);
		final double sharedAirspaceWeight3 = MathHelper.clamp(sharedAirspace / 10.0f, 0.0f, 1.0f);

		sendCutoff0 = Math.exp(-occlusionAccumulation * absorptionCoeff * 1.0f) * (1.0f - sharedAirspaceWeight0) + sharedAirspaceWeight0;
		sendCutoff1 = Math.exp(-occlusionAccumulation * absorptionCoeff * 1.0f) * (1.0f - sharedAirspaceWeight1) + sharedAirspaceWeight1;
		sendCutoff2 = Math.exp(-occlusionAccumulation * absorptionCoeff * 1.5f) * (1.0f - sharedAirspaceWeight2) + sharedAirspaceWeight2;
		sendCutoff3 = Math.exp(-occlusionAccumulation * absorptionCoeff * 1.5f) * (1.0f - sharedAirspaceWeight3) + sharedAirspaceWeight3;

		// attempt to preserve directionality when airspace is shared by allowing some dry signal through but filtered
		final double averageSharedAirspace = (sharedAirspaceWeight0 + sharedAirspaceWeight1 + sharedAirspaceWeight2 + sharedAirspaceWeight3) * 0.25f;
		directCutoff = Math.max(Math.pow(averageSharedAirspace, 0.5) * 0.2f, directCutoff);

		directGain = Math.pow(directCutoff, 0.1);

		//logDetailed("HitRatio0: " + hitRatioBounce1 + " HitRatio1: " + hitRatioBounce2 + " HitRatio2: " + hitRatioBounce3 + " HitRatio3: " + hitRatioBounce4);

		if (pC.eLog) logEnvironment("Bounce reflectivity 0: " + bounceReflectivityRatio[0] + " bounce reflectivity 1: " + bounceReflectivityRatio[1] + " bounce reflectivity 2: " + bounceReflectivityRatio[2] + " bounce reflectivity 3: " + bounceReflectivityRatio[3]);


		sendGain1 *= bounceReflectivityRatio[1];
		sendGain2 *= Math.pow(bounceReflectivityRatio[2], 3.0);
		sendGain3 *= Math.pow(bounceReflectivityRatio[3], 4.0);

		sendGain0 = MathHelper.clamp(sendGain0, 0.0f, 1.0f);
		sendGain1 = MathHelper.clamp(sendGain1, 0.0f, 1.0f);
		sendGain2 = MathHelper.clamp(sendGain2 * 1.05f - 0.05f, 0.0f, 1.0f);
		sendGain3 = MathHelper.clamp(sendGain3 * 1.05f - 0.05f, 0.0f, 1.0f);

		sendGain0 *= Math.pow(sendCutoff0, 0.1);
		sendGain1 *= Math.pow(sendCutoff1, 0.1);
		sendGain2 *= Math.pow(sendCutoff2, 0.1);
		sendGain3 *= Math.pow(sendCutoff3, 0.1);

		if (pC.eLog) logEnvironment("Final environment settings:   " + sendGain0 + ",   " + sendGain1 + ",   " + sendGain2 + ",   " + sendGain3);

		assert mc.player != null;
		if (mc.player.isSubmergedInWater())
		{
			sendCutoff0 *= 0.4f;
			sendCutoff1 *= 0.4f;
			sendCutoff2 *= 0.4f;
			sendCutoff3 *= 0.4f;
		}
		setEnvironment(sourceID, sendGain0, sendGain1, sendGain2, sendGain3, sendCutoff0, sendCutoff1, sendCutoff2, sendCutoff3, directCutoff, directGain);

	}

}
