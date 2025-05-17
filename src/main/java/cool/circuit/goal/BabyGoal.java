package cool.circuit.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

import static cool.circuit.DancingGiants.getEntityIds;
import static cool.circuit.commands.GiantCommand.clothes;

public class BabyGoal extends Goal {
    private final Giant adult;
    private final Zombie baby;
    private int ticks = 0;

    public BabyGoal(Giant adult, Zombie baby) {
        this.adult = adult;
        this.baby = baby;
        baby.setBaby(true);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    public boolean isBabyFullyGrown() {
        return ticks >= 10 * 20;
    }

    public boolean isBabySomewhatGrown() {
        return ticks >= 5 * 20;
    }

    public void baby() {
        Giant giant = new Giant(EntityType.GIANT, adult.level());
        giant.goalSelector.addGoal(0, new DanceGoal(giant));
        giant.goalSelector.addGoal(2, new RandomStrollGoal(giant, 1.0));
        giant.setPos(baby.position());

        int clothesIndex = (int) (Math.random() * clothes.size());

        for (Map.Entry<EquipmentSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
            giant.setItemSlot(entry.getKey(), entry.getValue());
        }

        adult.level().addFreshEntity(giant);

        getEntityIds().add(giant.getId());
    }

    @Override
    public void tick() {
        baby.getNavigation().moveTo(adult, 1.0);
        ticks++;
        if(isBabySomewhatGrown()) {
            baby.setBaby(false);
        }

        if(isBabyFullyGrown()) {
            baby();
            baby.discard();
        }
    }
}
