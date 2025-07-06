package net.tyler.tutorialmod.entity.ai;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.tyler.tutorialmod.entity.custom.HamsterEntity;
import net.tyler.tutorialmod.item.ModItems;

import java.util.EnumSet;

public class HamsterPlayDeadGoal extends Goal {
    private final HamsterEntity hamster;
    private int timer;
    private boolean wasSitting; // Remember if hamster was sitting before playing dead

    public HamsterPlayDeadGoal(HamsterEntity hamster){
        this.hamster = hamster;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));   // tells minecraft which parts of the entity is being controlled by this goal. to prevent conflicting goals from interfering with these controls.
    }

    @Override
    public boolean canStart() {
        if (!hamster.isTamed() || hamster.getOwner() == null) return false;     // short circuit if hamster is wild and if its owner is not in the world.
        if (hamster.playDeadCooldown > 0 || hamster.isPlayingDead()) return false;     // also dont start if it hasnt been past the cooldown time since last time of playing dead, or if     currently playing dead.

        PlayerEntity owner = (PlayerEntity) hamster.getOwner();

        return owner.isAlive()
                && hamster.squaredDistanceTo(owner) < 10 * 10
                && owner.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.CARVED_PUMPKIN);
    }

    @Override
    public void start() {
        super.start();
        timer = 20;     // how long to play dead

        // Remember sitting state and temporarily disable it
        wasSitting = hamster.isSitting();
        if (wasSitting) {
            hamster.setSitting(false);
        }

        hamster.setPlayingDead(true);       // trigger animation state in entity

        if (!hamster.getWorld().isClient()){
            hamster.dropItem(ModItems.FEAR_SHARD);
        }
    }

    @Override
    public void stop() {
        hamster.setPlayingDead(false);
        hamster.playDeadCooldown = 200; // 10 seconds cooldown

        // Restore sitting state if hamster was sitting before
        if (wasSitting) {
            hamster.setSitting(true);
        }

        super.stop();
    }

    @Override
    public void tick() {
        super.tick();
        if (timer-- < 0){
            this.stop();
        }
    }

    @Override
    public boolean shouldContinue() {
        return timer > 0 && hamster.isPlayingDead();
    }
}