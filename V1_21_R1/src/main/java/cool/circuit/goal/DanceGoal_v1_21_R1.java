package cool.circuit.goal;

import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import org.bukkit.Color;
import org.bukkit.Particle;

public class DanceGoal_v1_21_R1 extends PathfinderGoal {

    private final EntityCreature mob;
    private boolean crouching = false;
    private int timer = 0;

    public DanceGoal_v1_21_R1(EntityCreature mob) {
        this.mob = mob;
    }

    @Override
    public boolean b() {
        return true;
    }

    @Override
    public boolean c() {
        return true;
    }

    @Override
    public void d() {
        timer = 20;
        mob.b(EntityPose.f);
    }

    @Override
    public void e() {
        mob.b(EntityPose.a);
    }

    @Override
    public void a() {

        mob.getBukkitEntity().getWorld().spawnParticle(Particle.DUST, mob.getBukkitEntity().getLocation(), 10, 10, 10, 10000, new Particle.DustOptions(Color.fromRGB(
                (int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255)
        ), 10f));
        if (--timer <= 0) {
            crouching = !crouching;
            mob.b(crouching ? EntityPose.f : EntityPose.a);
            timer = 20;
        }
    }
}
