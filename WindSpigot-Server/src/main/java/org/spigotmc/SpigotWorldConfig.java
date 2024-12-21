package org.spigotmc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotWorldConfig {

	private final String worldName;
	private final YamlConfiguration config;
	private boolean verbose;

	public SpigotWorldConfig(String worldName) {
		this.worldName = worldName;
		this.config = SpigotConfig.config;
		init();
	}

	public void init() {
		this.verbose = getBoolean("verbose", true);

		SpigotConfig.readConfig(SpigotWorldConfig.class, this);
	}

	private void log(String s) {
		if (verbose) {
			Bukkit.getLogger().info(s);
		}
	}

	private void set(String path, Object val) {
		config.set("world-settings.default." + path, val);
	}

	private boolean getBoolean(String path, boolean def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getBoolean("world-settings." + worldName + "." + path,
				config.getBoolean("world-settings.default." + path));
	}

	private double getDouble(String path, double def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getDouble("world-settings." + worldName + "." + path,
				config.getDouble("world-settings.default." + path));
	}

	private int getInt(String path, int def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getInt("world-settings." + worldName + "." + path,
				config.getInt("world-settings.default." + path));
	}

	private <T> List getList(String path, T def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getList("world-settings." + worldName + "." + path,
				config.getList("world-settings.default." + path));
	}

	private String getString(String path, String def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getString("world-settings." + worldName + "." + path,
				config.getString("world-settings.default." + path));
	}

	public int chunksPerTick;
	public boolean clearChunksOnTick;

	private void chunksPerTick() {
		chunksPerTick = getInt("chunks-per-tick", 650);

		clearChunksOnTick = getBoolean("clear-tick-list", false);
	}

	// Crop growth rates
	public int cactusModifier;
	public int caneModifier;
	public int melonModifier;
	public int mushroomModifier;
	public int pumpkinModifier;
	public int saplingModifier;
	public int wheatModifier;
	public int wartModifier;

	private int getAndValidateGrowth(String crop) {
		int modifier = getInt("growth." + crop.toLowerCase() + "-modifier", 100);
		if (modifier == 0) {
			modifier = 100;
		}

		return modifier;
	}

	private void growthModifiers() {
		cactusModifier = getAndValidateGrowth("Cactus");
		caneModifier = getAndValidateGrowth("Cane");
		melonModifier = getAndValidateGrowth("Melon");
		mushroomModifier = getAndValidateGrowth("Mushroom");
		pumpkinModifier = getAndValidateGrowth("Pumpkin");
		saplingModifier = getAndValidateGrowth("Sapling");
		wheatModifier = getAndValidateGrowth("Wheat");
		wartModifier = getAndValidateGrowth("NetherWart");
	}

	public double itemMerge;

	private void itemMerge() {
		// WindSpigot - optimize config defaults 2.5 -> 3.5
		itemMerge = getDouble("merge-radius.item", 3.5);
	}

	public double expMerge;

	private void expMerge() {
		// WindSpigot - optimize config defaults 3.0 -> 4.0
		expMerge = getDouble("merge-radius.exp", 4.0);
	}

	public int viewDistance;

	private void viewDistance() {
		viewDistance = getInt("view-distance", Bukkit.getViewDistance());
	}

	public byte mobSpawnRange;

	private void mobSpawnRange() {
		// WindSpigot - optimize config defaults 4 -> 2
		mobSpawnRange = (byte) getInt("mob-spawn-range", 2);
		log("Mob Spawn Range: " + mobSpawnRange);
	}

	// WindSpigot - optimize config defaults 32 -> 16, 32 -> 24, 16 -> 8
	public int animalActivationRange = 16;
	public int monsterActivationRange = 24;
	public int miscActivationRange = 8;

	private void activationRange() {
		animalActivationRange = getInt("entity-activation-range.animals", animalActivationRange);
		monsterActivationRange = getInt("entity-activation-range.monsters", monsterActivationRange);
		miscActivationRange = getInt("entity-activation-range.misc", miscActivationRange);
	}

	public int playerTrackingRange = 48;
	public int animalTrackingRange = 48;
	public int monsterTrackingRange = 48;
	public int miscTrackingRange = 32;
	public int otherTrackingRange = 64;

	private void trackingRange() {
		playerTrackingRange = getInt("entity-tracking-range.players", playerTrackingRange);
		animalTrackingRange = getInt("entity-tracking-range.animals", animalTrackingRange);
		monsterTrackingRange = getInt("entity-tracking-range.monsters", monsterTrackingRange);
		miscTrackingRange = getInt("entity-tracking-range.misc", miscTrackingRange);
		otherTrackingRange = getInt("entity-tracking-range.other", otherTrackingRange);
	}

	public int hopperTransfer;
	public int hopperCheck;
	public int hopperAmount;

	private void hoppers() {
		// Set the tick delay between hopper item movements
		hopperTransfer = getInt("ticks-per.hopper-transfer", 8);
		// Set the tick delay between checking for items after the associated
		// container is empty. Default to the hopperTransfer value to prevent
		// hopper sorting machines from becoming out of sync.
		hopperCheck = getInt("ticks-per.hopper-check", hopperTransfer);
		hopperAmount = getInt("hopper-amount", 1);
	}

	public boolean randomLightUpdates;

	private void lightUpdates() {
		randomLightUpdates = getBoolean("random-light-updates", false);
	}

	public boolean saveStructureInfo;

	private void structureInfo() {
		saveStructureInfo = getBoolean("save-structure-info", true);
	}

	public int itemDespawnRate;

	private void itemDespawnRate() {
		itemDespawnRate = getInt("item-despawn-rate", 6000);
	}

	public int arrowDespawnRate;

	private void arrowDespawnRate() {
		arrowDespawnRate = getInt("arrow-despawn-rate", 1200);
	}

	public boolean antiXray;
	public int engineMode;
	public List<Integer> hiddenBlocks;
	public List<Integer> replaceBlocks;
	public AntiXray antiXrayInstance;

	private void antiXray() {
		antiXray = getBoolean("anti-xray.enabled", false);
		log("Anti X-Ray: " + antiXray);

		engineMode = getInt("anti-xray.engine-mode", 1);
		log("\tEngine Mode: " + engineMode);

		if (SpigotConfig.version < 5) {
			set("anti-xray.blocks", null);
		}
		hiddenBlocks = getList("anti-xray.hide-blocks",
				Arrays.asList(14, 15, 16, 21, 48, 49, 54, 56, 73, 74, 82, 129, 130));

		replaceBlocks = getList("anti-xray.replace-blocks", Arrays.asList(1, 5));

		antiXrayInstance = new AntiXray(this);
	}

	public boolean zombieAggressiveTowardsVillager;

	private void zombieAggressiveTowardsVillager() {
		zombieAggressiveTowardsVillager = getBoolean("zombie-aggressive-towards-villager", true);
	}

	public boolean nerfSpawnerMobs;

	private void nerfSpawnerMobs() {
		// WindSpigot - optimize config defaults false -> true
		nerfSpawnerMobs = getBoolean("nerf-spawner-mobs", true);
		log("Nerfing mobs spawned from spawners: " + nerfSpawnerMobs);
	}

	public boolean enableZombiePigmenPortalSpawns;

	private void enableZombiePigmenPortalSpawns() {
		enableZombiePigmenPortalSpawns = getBoolean("enable-zombie-pigmen-portal-spawns", true);
	}

	public int maxBulkChunk;

	private void bulkChunkCount() {
		maxBulkChunk = getInt("max-bulk-chunks", 10);
	}

	public int maxCollisionsPerEntity;

	private void maxEntityCollision() {
		maxCollisionsPerEntity = getInt("max-entity-collisions", 4);
	}

	public int dragonDeathSoundRadius;

	private void keepDragonDeathPerWorld() {
		dragonDeathSoundRadius = getInt("dragon-death-sound-radius", 0);
	}

	public int witherSpawnSoundRadius;

	private void witherSpawnSoundRadius() {
		witherSpawnSoundRadius = getInt("wither-spawn-sound-radius", 0);
	}

	public int villageSeed;
	public int largeFeatureSeed;

	private void initWorldGenSeeds() {
		villageSeed = getInt("seed-village", 10387312);
		largeFeatureSeed = getInt("seed-feature", 14357617);
	}

	public float walkExhaustion;
	public float sprintExhaustion;
	public float combatExhaustion;
	public float regenExhaustion;

	private void initHunger() {
		walkExhaustion = (float) getDouble("hunger.walk-exhaustion", 0.2);
		sprintExhaustion = (float) getDouble("hunger.sprint-exhaustion", 0.8);
		combatExhaustion = (float) getDouble("hunger.combat-exhaustion", 0.3);
		regenExhaustion = (float) getDouble("hunger.regen-exhaustion", 3);
	}

	public int currentPrimedTnt = 0;
	public int maxTntTicksPerTick;

	private void maxTntPerTick() {
		if (SpigotConfig.version < 7) {
			set("max-tnt-per-tick", 100);
		}
		maxTntTicksPerTick = getInt("max-tnt-per-tick", 100);
	}

	public int hangingTickFrequency;

	private void hangingTickFrequency() {
		hangingTickFrequency = getInt("hanging-tick-frequency", 100);
	}
}
