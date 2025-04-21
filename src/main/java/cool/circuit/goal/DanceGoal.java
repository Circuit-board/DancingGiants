package cool.circuit.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;

public final class DanceGoal extends Goal {

    private final PathfinderMob mob;
    private boolean crouching = false;
    private int timer = 0;

    public DanceGoal(final PathfinderMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return true; // Always allow the goal to run
    }

    @Override
    public boolean canContinueToUse() {
        return true; // Keep running forever
    }

    @Override
    public void start() {
        timer = 20; // Start with a delay of 1 second
        mob.setPose(Pose.STANDING); // Start standing
    }

    @Override
    public void stop() {
        mob.setPose(Pose.STANDING); // Reset when stopping
    }

    @Override
    public void tick() {
        if (--timer <= 0) {
            crouching = !crouching;
            mob.setPose(crouching ? Pose.CROUCHING : Pose.STANDING);
            timer = 20; // Reset timer for 1 second
        }
    }
}
