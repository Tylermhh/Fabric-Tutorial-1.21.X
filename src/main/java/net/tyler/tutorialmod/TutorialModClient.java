package net.tyler.tutorialmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.tyler.tutorialmod.entity.ModEntities;
import net.tyler.tutorialmod.entity.client.HamsterModel;
import net.tyler.tutorialmod.entity.client.HamsterRenderer;
import net.tyler.tutorialmod.entity.client.MantisModel;
import net.tyler.tutorialmod.entity.client.MantisRenderer;

public class TutorialModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // Two things we always need when you have any sort of rendering
        EntityModelLayerRegistry.registerModelLayer(MantisModel.MANTIS, MantisModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.MANTIS, MantisRenderer::new);

        // for my hamster
        EntityModelLayerRegistry.registerModelLayer(HamsterModel.HAMSTER, HamsterModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.HAMSTER, HamsterRenderer::new);
    }
}
