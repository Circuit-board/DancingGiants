package cool.circuit.goal;

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

public class BabyGrowGoal extends Goal {
    private final Zombie baby;
    private int ticks = 0;

    public BabyGrowGoal(Zombie baby) {
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
        Giant giant = new Giant(EntityType.GIANT, baby.level());
        giant.goalSelector.addGoal(0, new DanceGoal(giant));
        giant.goalSelector.addGoal(2, new RandomStrollGoal(giant, 1.0));
        giant.setPos(baby.position());

        int clothesIndex = (int) (Math.random() * clothes.size());

        for (Map.Entry<EquipmentSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
            giant.setItemSlot(entry.getKey(), entry.getValue());
        }

        baby.level().addFreshEntity(giant);

        getEntityIds().add(giant.getId());
    }

    @Override
    public void tick() {
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
