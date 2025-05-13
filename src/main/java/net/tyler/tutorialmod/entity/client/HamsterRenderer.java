package net.tyler.tutorialmod.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.tyler.tutorialmod.TutorialMod;
import net.tyler.tutorialmod.entity.custom.HamsterEntity;

public class HamsterRenderer extends MobEntityRenderer<HamsterEntity, HamsterModel<HamsterEntity>> {
    public HamsterRenderer(EntityRendererFactory.Context context) {
        super(context, new HamsterModel<>(context.getPart(HamsterModel.HAMSTER)), 0.25f);       // last param is shadow radius
    }

    @Override
    public Identifier getTexture(HamsterEntity entity) {
        return Identifier.of(TutorialMod.MOD_ID, "textures/entity/hamster/hamster.png");
    }

    @Override
    public void render(HamsterEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
