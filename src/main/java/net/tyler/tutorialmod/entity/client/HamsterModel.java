package net.tyler.tutorialmod.entity.client;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.tyler.tutorialmod.TutorialMod;
import net.tyler.tutorialmod.entity.custom.HamsterEntity;

public class HamsterModel<T extends HamsterEntity>  extends SinglePartEntityModel<T> {
    public static final EntityModelLayer HAMSTER = new EntityModelLayer(Identifier.of(TutorialMod.MOD_ID, "hamster"), "main");

    private final ModelPart hamster;
    private final ModelPart head;

    public HamsterModel(ModelPart root) {
        this.hamster = root.getChild("hamster");
        this.head = this.hamster.getChild("head");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hamster = modelPartData.addChild("hamster", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

        ModelPartData body = hamster.addChild("body", ModelPartBuilder.create().uv(4, 0).cuboid(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData head = hamster.addChild("head", ModelPartBuilder.create().uv(1, 14).cuboid(-3.0F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 23).cuboid(-4.0F, -1.0F, -3.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
                .uv(6, 23).cuboid(2.0F, -1.0F, -3.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.5F, -1.0F, -3.0F));

        ModelPartData ears = head.addChild("ears", ModelPartBuilder.create().uv(0, 3).cuboid(-2.5F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(0.5F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, -2.0F, -3.0F));

        ModelPartData frontleg1 = hamster.addChild("frontleg1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, -3.5F));

        ModelPartData leftleg = frontleg1.addChild("leftleg", ModelPartBuilder.create().uv(0, 6).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, 0.0F));

        ModelPartData rightleg = frontleg1.addChild("rightleg", ModelPartBuilder.create().uv(0, 6).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, 0.0F));

        ModelPartData backleg = hamster.addChild("backleg", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, 3.5F));

        ModelPartData rightleg2 = backleg.addChild("rightleg2", ModelPartBuilder.create().uv(0, 6).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, 0.0F));

        ModelPartData leftleg2 = backleg.addChild("leftleg2", ModelPartBuilder.create().uv(0, 6).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, 0.0F));

        ModelPartData tail = hamster.addChild("tail", ModelPartBuilder.create().uv(6, 0).cuboid(-0.5F, 0.1F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 4.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }
    @Override
    public void setAngles(HamsterEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);       // SUPER IMPORTANT always needed. otherwise all transformations will be additive and you will keep rotating left and break your neck.
        this.setHeadAngles(netHeadYaw, headPitch);      // set the head angle

        // see MantisModel for explanations
        this.animateMovement(HamsterAnimations.ANIM_HAMSTER_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, HamsterAnimations.ANIM_HAMSTER_IDLE, ageInTicks, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);         // looking left and right. limit angles
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);         // looking up and down. limit angles

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        hamster.render(matrices, vertexConsumer, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return hamster;
    }
}
