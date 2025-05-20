package cool.circuit.goal;

import cool.circuit.Instance;
import cool.circuit.SwingGoal;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import org.bukkit.scheduler.BukkitRunnable;

public class SwingGoal_v1_21_R1 extends PathfinderGoal implements SwingGoal {

    private boolean isSwinging = true;

    public SwingGoal_v1_21_R1(EntityGiantZombie giant) {

        new BukkitRunnable() {

            @Override
            public void run() {
                if(isSwinging)
                    giant.a(EnumHand.a);
            }
        }.runTaskTimer(Instance.getInstance(), 0L, 20L);
    }

    @Override
    public boolean b() {
        return true;
    }

    @Override
    public void setSwinging(boolean swinging) {
        this.isSwinging = swinging;
    }

    @Override
    public boolean isSwinging() {
        return isSwinging;
    }
}
