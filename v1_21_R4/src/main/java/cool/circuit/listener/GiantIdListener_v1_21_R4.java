package cool.circuit.listener;

import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import static cool.circuit.Variables.ENTITY_IDS;

public class GiantIdListener_v1_21_R4 implements Listener {
    @EventHandler
    public void onInteractWithGiant(PlayerInteractAtEntityEvent event) {
        if(!(event.getPlayer().isSneaking())) return;

        if(!(((CraftEntity)event.getRightClicked()).getHandle() instanceof EntityGiantZombie) && !(((CraftEntity)event.getRightClicked()).getHandle() instanceof EntityZombie)) return;

        int id = (((CraftEntity)event.getRightClicked()).getHandle() instanceof EntityGiantZombie ? (EntityGiantZombie) ((CraftEntity)event.getRightClicked()).getHandle() : (EntityZombie) ((CraftEntity)event.getRightClicked()).getHandle()).ao();

        if(!(ENTITY_IDS.contains(id))) return;

        event.getPlayer().sendMessage(ChatColor.GREEN + "Entity Id: " + id);
    }
}
