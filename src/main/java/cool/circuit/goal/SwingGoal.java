package cool.circuit.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Giant;
import org.bukkit.scheduler.BukkitRunnable;

import static cool.circuit.DancingGiants.getInstance;

public class SwingGoal extends Goal {

    private boolean isSwinging = true;

    public SwingGoal(Giant giant) {

        new BukkitRunnable() {

            @Override
            public void run() {
                if(isSwinging)
                    giant.swing(InteractionHand.MAIN_HAND);
            }
        }.runTaskTimer(getInstance(), 0L, 20L);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    public void setSwinging(boolean swinging) {
        this.isSwinging = swinging;
    }

    public boolean isSwinging() {
        return isSwinging;
    }
}
