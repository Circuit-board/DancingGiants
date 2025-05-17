package cool.circuit.listener;

import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static cool.circuit.DancingGiants.getEntityIds;

public class DeathListener implements Listener {
    @EventHandler
    public void onGiantDie(EntityDeathEvent event) {
        int id = ((CraftEntity) event.getEntity()).getHandle().getId();
        if(getEntityIds().contains(id)) {
            getEntityIds().remove(id);
        }
    }
}
