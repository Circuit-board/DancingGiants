package cool.circuit;

import net.minecraft.world.entity.Entity;

public class EntityIdGetter_v1_21_R4 {
    public static int entityId(Entity entity) {
        return entity.ao();
    }
}
