package net.tyler.tutorialmod;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.tyler.tutorialmod.block.ModBlocks;
import net.tyler.tutorialmod.effect.ModEffects;
import net.tyler.tutorialmod.entity.ModEntities;
import net.tyler.tutorialmod.entity.custom.HamsterEntity;
import net.tyler.tutorialmod.entity.custom.MantisEntity;
import net.tyler.tutorialmod.item.ModItemGroups;
import net.tyler.tutorialmod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TutorialMod implements ModInitializer {
	public static final String MOD_ID = "tutorialmod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItemGroups.registerItemGroups();

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEffects.registerEffects();

		ModEntities.registerModEntities();

		FabricDefaultAttributeRegistry.register(ModEntities.MANTIS, MantisEntity.createAttributes());

		// for my hamster
		FabricDefaultAttributeRegistry.register(ModEntities.HAMSTER, HamsterEntity.createAttributes());
	}
}