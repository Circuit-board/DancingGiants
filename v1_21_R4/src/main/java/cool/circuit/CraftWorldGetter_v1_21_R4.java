package cool.circuit;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;

public class CraftWorldGetter_v1_21_R4 {
    public static net.minecraft.world.level.World craftWorld(World world) {
        return ((CraftWorld) world).getHandle();
    }
}
