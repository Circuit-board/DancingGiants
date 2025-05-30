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
import static cool.circuit.Variables.ENTITY_IDS;

public class BabyGrowGoal_v1_21_R1 extends PathfinderGoal {
    private final EntityZombie baby;
    private int ticks = 0;

    public BabyGrowGoal_v1_21_R1(EntityZombie baby) {
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
        EntityGiantZombie giant = new EntityGiantZombie(EntityTypes.U, baby.dO());
        giant.bW.a(0, new DanceGoal_v1_21_R1(giant));
        giant.bW.a(2, new PathfinderGoalRandomStroll(giant, 1.0));
        giant.a_(baby.dt(), baby.dv(), baby.dz());

        int clothesIndex = (int) (Math.random() * clothes.size());

        for (Map.Entry<EnumItemSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
            giant.setItemSlot(entry.getKey(), entry.getValue(), true);
        }

        baby.dO().addFreshEntity(giant, CreatureSpawnEvent.SpawnReason.COMMAND);
        ENTITY_IDS.add(giant.an());
    }

    @Override
    public void a() {
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
