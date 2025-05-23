package net.tyler.tutorialmod.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.tyler.tutorialmod.entity.ModEntities;
import org.jetbrains.annotations.Nullable;

public class HamsterEntity extends TameableEntity {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;

    public HamsterEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(false, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MoveIntoWaterGoal(this));
        this.goalSelector.add(0, new TameableEntity.TameableEscapeDangerGoal(1.5, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));


        this.goalSelector.add(1, new SitGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));

        this.goalSelector.add(2, new TemptGoal(this, 1.3D, Ingredient.ofItems(Items.SWEET_BERRIES), true));

        this.goalSelector.add(3, new FollowParentGoal(this, 2D));
        this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));

        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.3D));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.3D, true));

        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));


        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, MantisEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, AnimalEntity.class, false, (livingEntity) -> !(livingEntity instanceof HamsterEntity)));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.2F)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.SWEET_BERRIES);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.HAMSTER.create(world);
    }


    // Tameable parts all below
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(HamsterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SITTING, false);
    }

    @Override
    public void setSitting(boolean sitting) {
        super.setSitting(sitting);
        this.dataTracker.set(SITTING, sitting);
    }

    @Override
    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("isSitting", this.dataTracker.get(SITTING));     // this "isSitting" is a new var name for the boolean in the nbt data storage. must be matched to get this data back when relaunching
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        boolean lastSavedSit = nbt.getBoolean("isSitting");
        this.dataTracker.set(SITTING, lastSavedSit);
    }

    @Override
    public void setTamed(boolean tamed, boolean updateAttributes) {
        super.setTamed(tamed, updateAttributes);
        if (tamed) {
            getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(60.0D);
            getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue((double)0.3F);
            getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(15f);
            getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);


        Item tameableItem = Items.SWEET_BERRIES;

        if (itemStack.getItem() == tameableItem && !isTamed()) {
            if (this.getWorld().isClient()) {
                return ActionResult.CONSUME;
            } else {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                if (!this.getWorld().isClient()) {
                    super.setOwner(player);
                    this.navigation.recalculatePath();
                    this.setTarget(null);
                    this.getWorld().sendEntityStatus(this, (byte)7); // responsible for the particle effects for when tamed
                    setSitting(true);
                }

                return ActionResult.SUCCESS;
            }
        }

        if (isTamed() && !this.getWorld().isClient() && hand == Hand.MAIN_HAND) {
            setSitting(!isSitting());
            return ActionResult.SUCCESS;
        }

        if (itemStack.getItem() == tameableItem) {
            return ActionResult.PASS;
        }

        return super.interactMob(player, hand);
    }
}
