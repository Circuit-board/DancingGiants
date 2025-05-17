package cool.circuit.goal;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;

public class DanceGoal extends Goal {

    private final PathfinderMob mob;
    private boolean crouching = false;
    private int timer = 0;

    public DanceGoal(final PathfinderMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }

    @Override
    public void start() {
        timer = 20;
        mob.setPose(Pose.STANDING);
    }

    @Override
    public void stop() {
        mob.setPose(Pose.STANDING);
    }

    @Override
    public void tick() {

        mob.getBukkitEntity().getWorld().spawnParticle(Particle.DUST, mob.getBukkitEntity().getLocation(), 10, 10, 10, 100, new Particle.DustOptions(Color.fromRGB(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255)
        ), 10f));
        if (--timer <= 0) {
            crouching = !crouching;
            mob.setPose(crouching ? Pose.CROUCHING : Pose.STANDING);
            timer = 20;
        }
    }
}
