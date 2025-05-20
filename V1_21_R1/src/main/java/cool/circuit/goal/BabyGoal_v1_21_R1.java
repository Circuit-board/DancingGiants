package cool.circuit.goal;


import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.item.ItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Map;

import static cool.circuit.GiantCommand_v1_21_R1.clothes;
import static cool.circuit.GiantCommand_v1_21_R1.getSwings;
import static cool.circuit.Variables.ENTITY_IDS;
import static cool.circuit.Variables.GOALS;

public class BabyGoal_v1_21_R1 extends PathfinderGoal {
    private final EntityGiantZombie adult;
    private final EntityZombie baby;
    private int ticks = 0;

    public BabyGoal_v1_21_R1(EntityGiantZombie adult, EntityZombie baby) {
        this.adult = adult;
        this.baby = baby;
        baby.a(true);
    }

    @Override
    public boolean b() {
        return true;
    }

    public boolean isBabyFullyGrown() {
        return ticks >= 10 * 20;
    }

    public boolean isBabySomewhatGrown() {
        return ticks >= 5 * 20;
    }

    public void baby() {
        EntityGiantZombie giant = new EntityGiantZombie(EntityTypes.U, adult.dO());
        giant.bW.a(0, new DanceGoal_v1_21_R1(giant));
        if(getSwings().get(adult)) {
            SwingGoal_v1_21_R1 goal = new SwingGoal_v1_21_R1(giant);
            GOALS.put(giant, goal);
            getSwings().put(giant, true);
            giant.bW.a(1, goal);
        }
        giant.bW.a(2, new PathfinderGoalRandomStroll(giant, 1.0));
        giant.a_(baby.dt(), baby.dv(), baby.dz());

        int clothesIndex = (int) (Math.random() * clothes.size());

        for (Map.Entry<EnumItemSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
            giant.setItemSlot(entry.getKey(), entry.getValue(), true);
        }

        adult.dO().addFreshEntity(giant, CreatureSpawnEvent.SpawnReason.COMMAND);
        ENTITY_IDS.add(giant.an());
    }

    @Override
    public void a() {
        baby.N().a(adult, 1.0);
        ticks++;
        if(isBabySomewhatGrown()) {
            baby.a(false);
        }

        if(isBabyFullyGrown()) {
            baby();
            baby.aq();
        }
    }
}
