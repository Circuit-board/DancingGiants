package cool.circuit.listener;

import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static cool.circuit.Variables.ENTITY_IDS;

public class DeathListener_v1_21_R4 implements Listener {
    @EventHandler
    public void onGiantDie(EntityDeathEvent event) {
        int id = ((CraftEntity) event.getEntity()).getHandle().ao();
        if(ENTITY_IDS.contains(id)) {
            ENTITY_IDS.remove(id);
        }
    }
}
