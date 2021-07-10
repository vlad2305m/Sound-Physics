package com.sonicether.soundphysics;

import java.util.Random;
import java.util.regex.Pattern;

import com.sonicether.soundphysics.config.ConfigManager;
import com.sonicether.soundphysics.config.ReverbParams;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;

import net.minecraft.world.RaycastContext;
import org.lwjgl.openal.*;

import static com.sonicether.soundphysics.RaycastFix.fixedRaycast;

public class SoundPhysics
{

	private static final Pattern rainPattern = Pattern.compile(".*rain.*");
	private static final Pattern stepPattern = Pattern.compile(".*step.*");
	private static final Pattern blockPattern = Pattern.compile(".*block..*");
	//Private fields
	private static final String logPrefix = "[SOUND PHYSICS]";
	private static int auxFXSlot0;
	private static int auxFXSlot1;
	private static int auxFXSlot2;
	private static int auxFXSlot3;
	private static int reverb0;
	private static int reverb1;
	private static int reverb2;
	private static int reverb3;
	private static int directFilter0;
	private static int sendFilter0;
	private static int sendFilter1;
	private static int sendFilter2;
	private static int sendFilter3;
	
	private static MinecraftClient mc;
	
	private static SoundCategory lastSoundCategory;
	private static String lastSoundName;

	private static Random rand;

	//Public fields
	public static float globalVolumeMultiplier = 4.0f;

	public static void init()
	{
		log("Initializing Sound Physics...");
		setupEFX();
		log("EFX ready...");
		mc = MinecraftClient.getInstance();
		rand = new Random(System.currentTimeMillis());
	}
	
	
	public static void syncReverbParams()
	{
		if (auxFXSlot0 != 0)
		{
			//Set the global reverb parameters and apply them to the effect and effectslot
			setReverbParams(ReverbParams.getReverb0(), auxFXSlot0, reverb0);
			setReverbParams(ReverbParams.getReverb1(), auxFXSlot1, reverb1);
			setReverbParams(ReverbParams.getReverb2(), auxFXSlot2, reverb2);
			setReverbParams(ReverbParams.getReverb3(), auxFXSlot3, reverb3);
		}
	}

	static void setupEFX()
	{
		//Get current context and device
		final long currentContext = ALC10.alcGetCurrentContext();
		final long currentDevice = ALC10.alcGetContextsDevice(currentContext);
		if (ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
			log("EFX Extension recognized.");
		} else {
			logError("EFX Extension not found on current device. Aborting.");
			return;
		}

		// Create auxiliary effect slots
		auxFXSlot0 = EXTEfx.alGenAuxiliaryEffectSlots();
		log("Aux slot " + auxFXSlot0 + " created");
		EXTEfx.alAuxiliaryEffectSloti(auxFXSlot0, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		
		auxFXSlot1 = EXTEfx.alGenAuxiliaryEffectSlots();
		log("Aux slot " + auxFXSlot1 + " created");
		EXTEfx.alAuxiliaryEffectSloti(auxFXSlot1, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		
		auxFXSlot2 = EXTEfx.alGenAuxiliaryEffectSlots();
		log("Aux slot " + auxFXSlot2 + " created");
		EXTEfx.alAuxiliaryEffectSloti(auxFXSlot2, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
		
		auxFXSlot3 = EXTEfx.alGenAuxiliaryEffectSlots();
		log("Aux slot " + auxFXSlot3 + " created");
		EXTEfx.alAuxiliaryEffectSloti(auxFXSlot3, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);	
		checkErrorLog("Failed creating auxiliary effect slots!");
		
		//Create effect objects
		reverb0 = EXTEfx.alGenEffects();												//Create effect object
		EXTEfx.alEffecti(reverb0, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);		//Set effect object to be reverb
		checkErrorLog("Failed creating reverb effect slot 0!");
		reverb1 = EXTEfx.alGenEffects();												//Create effect object
		EXTEfx.alEffecti(reverb1, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);		//Set effect object to be reverb
		checkErrorLog("Failed creating reverb effect slot 1!");
		reverb2 = EXTEfx.alGenEffects();												//Create effect object
		EXTEfx.alEffecti(reverb2, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);		//Set effect object to be reverb
		checkErrorLog("Failed creating reverb effect slot 2!");
		reverb3 = EXTEfx.alGenEffects();												//Create effect object
		EXTEfx.alEffecti(reverb3, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_EAXREVERB);		//Set effect object to be reverb
		checkErrorLog("Failed creating reverb effect slot 3!");
		
		// Create filters
		directFilter0 = EXTEfx.alGenFilters();
		EXTEfx.alFilteri(directFilter0, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
		logGeneral("directFilter0: "+directFilter0);
		
		sendFilter0 = EXTEfx.alGenFilters();
		EXTEfx.alFilteri(sendFilter0, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
		logGeneral("filter0: "+sendFilter0);
		
		sendFilter1 = EXTEfx.alGenFilters();
		EXTEfx.alFilteri(sendFilter1, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
		logGeneral("filter1: "+sendFilter1);
		
		sendFilter2 = EXTEfx.alGenFilters();
		EXTEfx.alFilteri(sendFilter2, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
		logGeneral("filter2: "+sendFilter2);
		
		sendFilter3 = EXTEfx.alGenFilters();
		EXTEfx.alFilteri(sendFilter3, EXTEfx.AL_FILTER_TYPE, EXTEfx.AL_FILTER_LOWPASS);
		logGeneral("filter3: "+sendFilter3);
		checkErrorLog("Error creating lowpass filters!");
		
		syncReverbParams();
	}
	
	public static void setLastSoundCategoryAndName(SoundCategory sc, String name)
	{//log("Set last sound category and name");
		lastSoundCategory = sc;
		lastSoundName = name;
	}
	
	public static void onPlaySound(double posX, double posY, double posZ, int sourceID)
	{
		logGeneral("On play sound... Sounrce ID: " + sourceID + " " + posX + ", " + posY + ", " + posZ + "    Sound category: " + lastSoundCategory.toString() + "    Sound name: " + lastSoundName);

		if (ConfigManager.getConfig().reloadReverb) {
			ConfigManager.reload();
			ConfigManager.getConfig().reloadReverb = false;
			ConfigManager.save();
		}

		long startTime = 0;
		long endTime;
		
		if (ConfigManager.getConfig().Misc.performanceLogging) startTime = System.nanoTime();

		evaluateEnvironment(sourceID, posX, posY, posZ);
		
		if (ConfigManager.getConfig().Misc.performanceLogging)
		{ endTime = System.nanoTime();
			log("Total calculation time for sound " + lastSoundName + ": " + (double)(endTime - startTime)/(double)1000000 + " milliseconds"); }
		
		
		//System.out.println(soundCategory.getCategoryName());
	}
	
	public static double calculateEntitySoundOffset(float standingEyeHeight, SoundEvent sound)
	{
		if (stepPattern.matcher(sound.getId().getPath()).matches())
		{//log("Offset entity say sound by " + entity.getEyeHeight());
			return 0.0;
		}
		return standingEyeHeight;
	}
	
	private static float getBlockReflectivity(final BlockPos blockPos)
	{
		assert mc.world != null;
		BlockState blockState = mc.world.getBlockState(blockPos);
		BlockSoundGroup soundType = blockState.getSoundGroup();
		
		double reflectivity = 0.5f;

		if (soundType == BlockSoundGroup.STONE)
			reflectivity = ConfigManager.getConfig().Material_Properties.stoneReflectivity;
		else if (soundType == BlockSoundGroup.WOOD)
			reflectivity = ConfigManager.getConfig().Material_Properties.woodReflectivity;
		else if (soundType == BlockSoundGroup.GRAVEL)
			reflectivity = ConfigManager.getConfig().Material_Properties.groundReflectivity;
		else if (soundType == BlockSoundGroup.GRASS || soundType == BlockSoundGroup.MOSS_BLOCK || soundType == BlockSoundGroup.MOSS_CARPET)//TODO sound groups
			reflectivity = ConfigManager.getConfig().Material_Properties.foliageReflectivity;
		else if (soundType == BlockSoundGroup.METAL)
			reflectivity = ConfigManager.getConfig().Material_Properties.metalReflectivity;
		else if (soundType == BlockSoundGroup.GLASS)
			reflectivity = ConfigManager.getConfig().Material_Properties.glassReflectivity;
		else if (soundType == BlockSoundGroup.WOOL)
			reflectivity = ConfigManager.getConfig().Material_Properties.clothReflectivity;
		else if (soundType == BlockSoundGroup.SAND)	
			reflectivity = ConfigManager.getConfig().Material_Properties.sandReflectivity;
		else if (soundType == BlockSoundGroup.SNOW)
			reflectivity = ConfigManager.getConfig().Material_Properties.snowReflectivity;
		else if (soundType == BlockSoundGroup.LADDER)
			reflectivity = ConfigManager.getConfig().Material_Properties.woodReflectivity;
		else if (soundType == BlockSoundGroup.ANVIL)
			reflectivity = ConfigManager.getConfig().Material_Properties.metalReflectivity;
		
		reflectivity *= ConfigManager.getConfig().General.globalBlockReflectance;
		
		return (float) reflectivity;
	}

	private static Vec3d reflect(Vec3d dir, Vec3d normal)
	{
		//dir - 2.0 * dot(normal, dir) * normal
		final double dot = dir.dotProduct(normal) * 2.0;
		
		final double x = dir.x - dot * normal.x;
		final double y = dir.y - dot * normal.y;
		final double z = dir.z - dot * normal.z;
		
		return new Vec3d(x, y, z);
	}

	private static Vec3d divide(Vec3d top, Vec3d bottom) {
		return new Vec3d(
				bottom.x == 0.0 ? Double.MAX_VALUE * Math.signum(top.x) : top.x / bottom.x,
				bottom.y == 0.0 ? Double.MAX_VALUE * Math.signum(top.y) : top.y / bottom.y,
				bottom.z == 0.0 ? Double.MAX_VALUE * Math.signum(top.z) : top.z / bottom.z);
	}

	private static Vec3d extendToAFace(final Vec3d vec, final Vec3d dir, final BlockPos targetBlock){
		// the side planes we need to hit
		final Vec3d target = new Vec3d(
				dir.x > 0 ? targetBlock.getX() : targetBlock.getX() + 1,
				dir.y > 0 ? targetBlock.getY() : targetBlock.getY() + 1,
				dir.z > 0 ? targetBlock.getZ() : targetBlock.getZ() + 1	);
		// how far do we need to go to reach each in dir's
		Vec3d lambda = divide(target.subtract(vec), dir);
		// 0 if already between the planes
		if(targetBlock.getX() <= vec.x && vec.x <= targetBlock.getX()+1)lambda = new Vec3d(0.0, lambda.y, lambda.z);
		if(targetBlock.getY() <= vec.y && vec.y <= targetBlock.getY()+1)lambda = new Vec3d(lambda.x, 0.0, lambda.z);
		if(targetBlock.getZ() <= vec.z && vec.z <= targetBlock.getZ()+1)lambda = new Vec3d(lambda.x, lambda.y, 0.0);
		//you will *cough* not *cough* get this.
		if(lambda.x < 0 || lambda.y < 0 || lambda.z < 0) { System.out.println("Congratulations, you broke SP!    "+ lambda+ vec+ dir + targetBlock.toShortString()); return vec.add(dir.multiply(0.1));}
		// we need to hit all planes to get to the block
		if(lambda.x >= lambda.y && lambda.x >= lambda.z) return new Vec3d( /*target.x*/vec.x + dir.x * lambda.x, vec.y + dir.y * lambda.x, vec.z + dir.z * lambda.x );
		if(lambda.y >= lambda.z && lambda.y >= lambda.x) return new Vec3d( vec.x + dir.x * lambda.y, /*target.y*/vec.y + dir.y * lambda.y, vec.z + dir.z * lambda.y );
		if(lambda.z >= lambda.x && lambda.z >= lambda.y) return new Vec3d( vec.x + dir.x * lambda.z, vec.y + dir.y * lambda.z, vec.z + dir.z * lambda.z/*target.z*/ );
		// this will never happen. ever.
		return vec;

	}

	private static void evaluateEnvironment(final int sourceID, final double posX, final double posY, final double posZ)
	{
		if (mc.player == null | mc.world == null | posY <= 0 | lastSoundCategory == SoundCategory.RECORDS)
		{
			//logDetailed("Menu sound!");
			
			setEnvironment(sourceID, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}

		final boolean isRain = rainPattern.matcher(lastSoundName).matches();

		if (ConfigManager.getConfig().Performance.skipRainOcclusionTracing && isRain)
		{
			setEnvironment(sourceID, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
			return;
		}

		float directCutoff;
		final float absorptionCoeff = (float) (ConfigManager.getConfig().General.globalBlockAbsorption * 3.0);
		
		//Direct sound occlusion
		Vec3d playerPos = mc.player.getPos();
			  playerPos = new Vec3d(playerPos.x, playerPos.y + mc.player.getEyeHeight(mc.player.getPose()), playerPos.z);
		final Vec3d soundPos = new Vec3d(posX, posY, posZ);
		Vec3d normalToPlayer = playerPos.subtract(soundPos).normalize();

		final BlockPos soundBlockPos = new BlockPos(soundPos.x, soundPos.y,soundPos.z);

		logGeneral("Player pos: " + playerPos.x + ", " + playerPos.y + ", " + playerPos.z + "      Sound Pos: " + soundPos.x + ", " + soundPos.y + ", " + soundPos.z + "       To player vector: " + normalToPlayer.x + ", " + normalToPlayer.y + ", " + normalToPlayer.z);
		double occlusionAccumulation = 0.0f;
		//Cast a ray from the source towards the player
		Vec3d rayOrigin = soundPos;
		//System.out.println(rayOrigin.toString());
		BlockPos lastBlockPos = soundBlockPos;
		final boolean _9ray = ConfigManager.getConfig().Vlads_Tweaks._9RayDirectOcclusion && (lastSoundCategory == SoundCategory.BLOCKS || blockPattern.matcher(lastSoundName).matches());
		final int nOccRays = _9ray ? 9 : 1;
		double occlusionAccMin = Double.MAX_VALUE;
		for (int j = 0; j < nOccRays; j++) {
			if(j > 0){
				final int jj = j - 1;
				rayOrigin = new Vec3d(soundBlockPos.getX() + 0.001 + 0.998 * (jj % 2), soundBlockPos.getY() + 0.001 + 0.998 * ((jj >> 1) % 2), soundBlockPos.getZ() + 0.001 + 0.998 * ((jj >> 2) % 2));
				lastBlockPos = soundBlockPos;
				normalToPlayer = playerPos.subtract(rayOrigin).normalize();
				occlusionAccumulation = 0.0f;
			}
			for (int i = 0; i < 10; i++) {
				BlockHitResult rayHit = fixedRaycast(new RaycastContext(rayOrigin, playerPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, lastBlockPos);
				lastBlockPos = rayHit.getBlockPos();
				//If we hit a block

				if (rayHit.getType() == HitResult.Type.MISS) {
					break;
				}

				final BlockPos blockHitPos = rayHit.getBlockPos();
				final Vec3d rayHitPos = extendToAFace(rayOrigin, normalToPlayer, blockHitPos); // Minecraft raytracing doesn't work as you expect, so we fix what it doesn't do
				final BlockState blockHit = mc.world.getBlockState(blockHitPos);

				float blockOcclusion = 1.0f;

				// Regardless to whether we hit from inside or outside
				final Vec3d dirVec = rayHitPos.subtract(blockHitPos.getX() + 0.5, blockHitPos.getY() + 0.5, blockHitPos.getZ() + 0.5);
				final Direction sideHit = Direction.getFacing(dirVec.x, dirVec.y, dirVec.z);

				//System.out.println("Hit "+sideHit.asString()+" of "+blockHit.getBlock().getTranslationKey()+"       "+rayHitPos.toString()+"              "+rayHit.getBlockPos().toShortString());
				if (!blockHit.isSideSolidFullSquare(mc.world, rayHit.getBlockPos(), sideHit)) {
					// log("not a solid block!");
					blockOcclusion *= ConfigManager.getConfig().Vlads_Tweaks.leakyBlocksOcclusionMultiplier;
				} else //System.out.println("Hard");

					logOcclusion(blockHit.getBlock().getTranslationKey() + "    " + rayHitPos.x + ", " + rayHitPos.y + ", " + rayHitPos.z);

				//Accumulate density
				occlusionAccumulation += blockOcclusion;

				rayOrigin = rayHitPos; //new Vec3d(rayHit.getPos().x + normalToPlayer.x * 0.1, rayHit.getPos().y + normalToPlayer.y * 0.1, rayHit.getPos().z + normalToPlayer.z * 0.1);

				logOcclusion("New trace position: " + rayOrigin.x + ", " + rayOrigin.y + ", " + rayOrigin.z);
			}
			occlusionAccMin = Math.min(occlusionAccMin, occlusionAccumulation);
		}
		occlusionAccumulation = Math.min(occlusionAccMin, ConfigManager.getConfig().Vlads_Tweaks.maxDirectOcclusionFromBlocks);
		directCutoff = (float) Math.exp(-occlusionAccumulation * absorptionCoeff);
		float directGain = (float) Math.pow(directCutoff, 0.1);
		
		logOcclusion("direct cutoff: " + directCutoff + "  direct gain:" + directGain);
		
		// Calculate reverb parameters for this sound
		float sendGain0 = 0.0f;
		float sendGain1 = 0.0f;
		float sendGain2 = 0.0f;
		float sendGain3 = 0.0f;
		
		float sendCutoff0 = 1.0f;
		float sendCutoff1 = 1.0f;
		float sendCutoff2 = 1.0f;
		float sendCutoff3 = 1.0f;

		if (mc.player.isSubmergedInWater())
		{
			directCutoff *= 1.0f - ConfigManager.getConfig().General.underwaterFilter;
		}

		if (isRain)
		{
			setEnvironment(sourceID, sendGain0, sendGain1, sendGain2, sendGain3, sendCutoff0, sendCutoff1, sendCutoff2, sendCutoff3, directCutoff, directGain);
			return;
		}
		
		
		// Shoot rays around sound

		final float maxDistance = 256.0f;
		
		final int numRays = ConfigManager.getConfig().Performance.environmentEvaluationRays;
		final int rayBounces = 4;

		final float[] bounceReflectivityRatio = new float[rayBounces];
		
		float sharedAirspace = 0.0f;
		
		final float rcpTotalRays = 1.0f / (numRays * rayBounces);
		final float rcpPrimaryRays = 1.0f / (numRays);

		for (int i = 0; i < numRays; i++)
		{
			final double longitude = 2.0 * Math.PI * rand.nextDouble();
			final double latitude = Math.asin(rand.nextDouble() * 2.0f - 1.0f);
			
			final Vec3d rayDir = new Vec3d(Math.cos(latitude) * Math.cos(longitude),
					Math.cos(latitude) * Math.sin(longitude), Math.sin(latitude));

			final Vec3d rayEnd = new Vec3d(soundPos.x + rayDir.x * maxDistance, soundPos.y + rayDir.y * maxDistance,
					soundPos.z + rayDir.z * maxDistance);

			BlockHitResult rayHit = fixedRaycast(new RaycastContext(soundPos, rayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, soundBlockPos);

			if (rayHit.getType() == HitResult.Type.BLOCK) {
				final double rayLength = soundPos.distanceTo(rayHit.getPos());
				
				// Additional bounces
				BlockPos lastHitBlock = rayHit.getBlockPos();
				Vec3d lastHitPos = extendToAFace(soundPos, rayDir, lastHitBlock);
				Vec3d lastHitNormal = new Vec3d(rayHit.getSide().getUnitVector());
				Vec3d lastRayDir = rayDir;
				
				float totalRayDistance = (float) rayLength;
				
				// Secondary ray bounces
				for (int j = 0; j < rayBounces; j++) {
					final Vec3d newRayDir = reflect(lastRayDir, lastHitNormal);
					final Vec3d newRayStart = lastHitPos; //new Vec3d(lastHitPos.x + lastHitNormal.x * 0.01, lastHitPos.y + lastHitNormal.y * 0.01, lastHitPos.z + lastHitNormal.z * 0.01);
					final Vec3d newRayEnd = new Vec3d(newRayStart.x + newRayDir.x * maxDistance,
							newRayStart.y + newRayDir.y * maxDistance, newRayStart.z + newRayDir.z * maxDistance);
					
					//log("New ray dir: " + newRayDir.xCoord + ", " + newRayDir.yCoord + ", " + newRayDir.zCoord);
					
					BlockHitResult newRayHit = fixedRaycast(new RaycastContext(newRayStart, newRayEnd, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, lastHitBlock);

					float energyTowardsPlayer = 0.25f;
					final float blockReflectivity = getBlockReflectivity(lastHitBlock);
					energyTowardsPlayer *= blockReflectivity * 0.75f + 0.25f;

					if (newRayHit.getType() == HitResult.Type.MISS) {
						totalRayDistance += lastHitPos.distanceTo(playerPos);
					} else {
						final Vec3d newRayHitPos = extendToAFace(newRayStart, newRayDir, newRayHit.getBlockPos());
						final double newRayLength = lastHitPos.distanceTo(newRayHitPos);

						bounceReflectivityRatio[j] += blockReflectivity;

						totalRayDistance += newRayLength;

						lastHitPos = newRayHitPos;
						lastHitNormal = new Vec3d(newRayHit.getSide().getUnitVector());
						lastRayDir = newRayDir;
						lastHitBlock = newRayHit.getBlockPos();

						// Cast one final ray towards the player. If it's
						// unobstructed, then the sound source and the player
						// share airspace.
						if (ConfigManager.getConfig().Performance.simplerSharedAirspaceSimulation && j == rayBounces - 1
								|| !ConfigManager.getConfig().Performance.simplerSharedAirspaceSimulation) {
							final Vec3d finalRayStart = new Vec3d(lastHitPos.x + lastHitNormal.x * 0.01,
									lastHitPos.y + lastHitNormal.y * 0.01, lastHitPos.z + lastHitNormal.z * 0.01);

							final BlockHitResult finalRayHit = fixedRaycast(new RaycastContext(finalRayStart, playerPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, mc.player), mc.world, null);

							if (finalRayHit.getType() == HitResult.Type.MISS) {
								//log("Secondary ray hit the player!");
								sharedAirspace += 1.0f;
							}
						}
					}
					
					final float reflectionDelay = (float) Math.max(totalRayDistance, 0.0) * 0.12f * blockReflectivity;
					
					final float cross0 = 1.0f - MathHelper.clamp(Math.abs(reflectionDelay - 0.0f), 0.0f, 1.0f);
					final float cross1 = 1.0f - MathHelper.clamp(Math.abs(reflectionDelay - 1.0f), 0.0f, 1.0f);
					final float cross2 = 1.0f - MathHelper.clamp(Math.abs(reflectionDelay - 2.0f), 0.0f, 1.0f);
					final float cross3 = MathHelper.clamp(reflectionDelay - 2.0f, 0.0f, 1.0f);

					sendGain0 += cross0 * energyTowardsPlayer * 6.4f * rcpTotalRays;
					sendGain1 += cross1 * energyTowardsPlayer * 12.8f * rcpTotalRays;
					sendGain2 += cross2 * energyTowardsPlayer * 12.8f * rcpTotalRays;
					sendGain3 += cross3 * energyTowardsPlayer * 12.8f * rcpTotalRays;
					
					// Nowhere to bounce off of, stop bouncing!
					if (newRayHit.getType() == HitResult.Type.MISS) { break; }
				}

				//log("Hit " + mc.theWorld.getBlock(rayHit.blockX, rayHit.blockY, rayHit.blockZ).getUnlocalizedName() + " at " + rayHit.hitVec.xCoord + ", " + rayHit.hitVec.yCoord + ", " + rayHit.hitVec.zCoord + " and travelled " + rayLength + " meters.");
			}
		}
		bounceReflectivityRatio[0] = bounceReflectivityRatio[0] / numRays;
		bounceReflectivityRatio[1] = bounceReflectivityRatio[1] / numRays;
		bounceReflectivityRatio[2] = bounceReflectivityRatio[2] / numRays;
		bounceReflectivityRatio[3] = bounceReflectivityRatio[3] / numRays;
		
		sharedAirspace *= 64.0f;
		
		if (ConfigManager.getConfig().Performance.simplerSharedAirspaceSimulation)
			sharedAirspace *= rcpPrimaryRays;
		else
			sharedAirspace *= rcpTotalRays;
		
		final float sharedAirspaceWeight0 = MathHelper.clamp(sharedAirspace / 20.0f, 0.0f, 1.0f);
		final float sharedAirspaceWeight1 = MathHelper.clamp(sharedAirspace / 15.0f, 0.0f, 1.0f);
		final float sharedAirspaceWeight2 = MathHelper.clamp(sharedAirspace / 10.0f, 0.0f, 1.0f);
		final float sharedAirspaceWeight3 = MathHelper.clamp(sharedAirspace / 10.0f, 0.0f, 1.0f);

		sendCutoff0 = (float) Math.exp(-occlusionAccumulation * absorptionCoeff * 1.0f) * (1.0f - sharedAirspaceWeight0) + sharedAirspaceWeight0;
		sendCutoff1 = (float) Math.exp(-occlusionAccumulation * absorptionCoeff * 1.0f) * (1.0f - sharedAirspaceWeight1) + sharedAirspaceWeight1;
		sendCutoff2 = (float) Math.exp(-occlusionAccumulation * absorptionCoeff * 1.5f) * (1.0f - sharedAirspaceWeight2) + sharedAirspaceWeight2;
		sendCutoff3 = (float) Math.exp(-occlusionAccumulation * absorptionCoeff * 1.5f) * (1.0f - sharedAirspaceWeight3) + sharedAirspaceWeight3;

		// attempt to preserve directionality when airspace is shared by allowing some of the dry signal through but filtered
		final float averageSharedAirspace = (sharedAirspaceWeight0 + sharedAirspaceWeight1 + sharedAirspaceWeight2 + sharedAirspaceWeight3) * 0.25f;
		directCutoff = Math.max((float)Math.pow(averageSharedAirspace, 0.5) * 0.2f, directCutoff);
		
		directGain = (float) Math.pow(directCutoff, 0.1);
		
		//logDetailed("HitRatio0: " + hitRatioBounce1 + " HitRatio1: " + hitRatioBounce2 + " HitRatio2: " + hitRatioBounce3 + " HitRatio3: " + hitRatioBounce4);
		
		logEnvironment("Bounce reflectivity 0: " + bounceReflectivityRatio[0] + " bounce reflectivity 1: " + bounceReflectivityRatio[1] + " bounce reflectivity 2: " + bounceReflectivityRatio[2] + " bounce reflectivity 3: " + bounceReflectivityRatio[3]);

		
		sendGain1 *= bounceReflectivityRatio[1];
		sendGain2 *= (float) Math.pow(bounceReflectivityRatio[2], 3.0);
		sendGain3 *= (float) Math.pow(bounceReflectivityRatio[3], 4.0);

		sendGain0 = MathHelper.clamp(sendGain0, 0.0f, 1.0f);
		sendGain1 = MathHelper.clamp(sendGain1, 0.0f, 1.0f);
		sendGain2 = MathHelper.clamp(sendGain2 * 1.05f - 0.05f, 0.0f, 1.0f);
		sendGain3 = MathHelper.clamp(sendGain3 * 1.05f - 0.05f, 0.0f, 1.0f);

		sendGain0 *= (float) Math.pow(sendCutoff0, 0.1);
		sendGain1 *= (float) Math.pow(sendCutoff1, 0.1);
		sendGain2 *= (float) Math.pow(sendCutoff2, 0.1);
		sendGain3 *= (float) Math.pow(sendCutoff3, 0.1);
		
		logEnvironment("Final environment settings:   " + sendGain0 + ",   " + sendGain1 + ",   " + sendGain2 + ",   " + sendGain3);

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
	
	private static void setEnvironment(final int sourceID, final float sendGain0, final float sendGain1,
									   final float sendGain2, final float sendGain3, final float sendCutoff0, final float sendCutoff1,
									   final float sendCutoff2, final float sendCutoff3, final float directCutoff, final float directGain)
	{
		if (!ConfigManager.getConfig().enabled) return;
		// Set reverb send filter values and set source to send to all reverb fx slots
		EXTEfx.alFilterf(sendFilter0, EXTEfx.AL_LOWPASS_GAIN, sendGain0);
		EXTEfx.alFilterf(sendFilter0, EXTEfx.AL_LOWPASS_GAINHF, sendCutoff0);
		AL11.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxFXSlot0, 1, sendFilter0);
		checkErrorLog("Set Environment filter0:");
		
		EXTEfx.alFilterf(sendFilter1, EXTEfx.AL_LOWPASS_GAIN, sendGain1);
		EXTEfx.alFilterf(sendFilter1, EXTEfx.AL_LOWPASS_GAINHF, sendCutoff1);
		AL11.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxFXSlot1, 1, sendFilter1);
		checkErrorLog("Set Environment filter1:");
		
		EXTEfx.alFilterf(sendFilter2, EXTEfx.AL_LOWPASS_GAIN, sendGain2);
		EXTEfx.alFilterf(sendFilter2, EXTEfx.AL_LOWPASS_GAINHF, sendCutoff2);
		AL11.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxFXSlot2, 1, sendFilter2);
		checkErrorLog("Set Environment filter2:");
		
		EXTEfx.alFilterf(sendFilter3, EXTEfx.AL_LOWPASS_GAIN, sendGain3);
		EXTEfx.alFilterf(sendFilter3, EXTEfx.AL_LOWPASS_GAINHF, sendCutoff3);
		AL11.alSource3i(sourceID, EXTEfx.AL_AUXILIARY_SEND_FILTER, auxFXSlot3, 1, sendFilter3);
		checkErrorLog("Set Environment filter3:");
		
		EXTEfx.alFilterf(directFilter0, EXTEfx.AL_LOWPASS_GAIN, directGain);
		EXTEfx.alFilterf(directFilter0, EXTEfx.AL_LOWPASS_GAINHF, directCutoff);
		AL10.alSourcei(sourceID, EXTEfx.AL_DIRECT_FILTER, directFilter0);
		checkErrorLog("Set Environment directFilter0:");
		
		AL10.alSourcef(sourceID, EXTEfx.AL_AIR_ABSORPTION_FACTOR, (float) ConfigManager.getConfig().General.airAbsorption);
		checkErrorLog("Set Environment airAbsorbtion:");
	}

	private static void setSoundPos(final int sourceID, final Vec3d pos) {// TODO sound pos
		if (!ConfigManager.getConfig().enabled) return;
		AL10.alSourcefv(sourceID, 4100, new float[]{(float)pos.x, (float)pos.y, (float)pos.z});
	}

	/*
	 * Applies the parameters in the enum ReverbParams to the main reverb effect.
	 */
	protected static void setReverbParams(final ReverbParams r, final int auxFXSlot, final int reverbSlot)
	{
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DENSITY, r.density);		//Set default parameters
		checkErrorLog("Error while assigning reverb density: " + r.density);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DIFFUSION, r.diffusion);		//Set default parameters
		checkErrorLog("Error while assigning reverb diffusion: " + r.diffusion);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_GAIN, r.gain);		//Set default parameters
		checkErrorLog("Error while assigning reverb gain: " + r.gain);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_GAINHF, r.gainHF);		//Set default parameters
		checkErrorLog("Error while assigning reverb gainHF: " + r.gainHF);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DECAY_TIME, r.decayTime);		//Set default parameters
		checkErrorLog("Error while assigning reverb decayTime: " + r.decayTime);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_DECAY_HFRATIO, r.decayHFRatio);		//Set default parameters
		checkErrorLog("Error while assigning reverb decayHFRatio: " + r.decayHFRatio);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_REFLECTIONS_GAIN, r.reflectionsGain);		//Set default parameters
		checkErrorLog("Error while assigning reverb reflectionsGain: " + r.reflectionsGain);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_LATE_REVERB_GAIN, r.lateReverbGain);		//Set default parameters
		checkErrorLog("Error while assigning reverb lateReverbGain: " + r.lateReverbGain);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_LATE_REVERB_DELAY, r.lateReverbDelay);		//Set default parameters
		checkErrorLog("Error while assigning reverb lateReverbDelay: " + r.lateReverbDelay);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_AIR_ABSORPTION_GAINHF, r.airAbsorptionGainHF);		//Set default parameters
		checkErrorLog("Error while assigning reverb airAbsorptionGainHF: " + r.airAbsorptionGainHF);
		EXTEfx.alEffectf(reverbSlot, EXTEfx.AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, r.roomRolloffFactor);		//Set default parameters
		checkErrorLog("Error while assigning reverb roomRolloffFactor: " + r.roomRolloffFactor);
		
		
		//Attach updated effect object
		EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverbSlot);
	}
	
	protected static void log(String message)
	{
		System.out.println(logPrefix + ": " + message);
	}
	
	protected static void logOcclusion(String message)
	{
		if (!ConfigManager.getConfig().Misc.occlusionLogging)
			return;
		
		System.out.println(logPrefix + " [OCCLUSION] " + ": " + message);
	}
	
	protected static void logEnvironment(String message)
	{
		if (!ConfigManager.getConfig().Misc.environmentLogging)
			return;
		
		System.out.println(logPrefix + " [ENVIRONMENT] " + ": " + message);
	}
	
	
	protected static void logGeneral(String message)
	{
		if (!ConfigManager.getConfig().Misc.debugLogging)
			return;
		
		System.out.println(logPrefix + ": " + message);
	}
	
	
	protected static void logError(String errorMessage)
	{
		System.out.println(logPrefix + " [ERROR]: " + errorMessage);
	}
	
	public static void checkErrorLog(final String errorMessage)
	{
		final int error = AL10.alGetError();
		if (error == AL10.AL_NO_ERROR) {
			return;
		}

		String errorName;

			errorName = switch (error) {
				case AL10.AL_INVALID_NAME -> "AL_INVALID_NAME";
				case AL10.AL_INVALID_ENUM -> "AL_INVALID_ENUM";
				case AL10.AL_INVALID_VALUE -> "AL_INVALID_VALUE";
				case AL10.AL_INVALID_OPERATION -> "AL_INVALID_OPERATION";
				case AL10.AL_OUT_OF_MEMORY -> "AL_OUT_OF_MEMORY";
				default -> Integer.toString(error);
			};
			
			logError(errorMessage + " OpenAL error " + errorName);
	}

}
