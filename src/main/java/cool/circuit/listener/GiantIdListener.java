package cool.circuit.listener;

import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Zombie;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import static cool.circuit.DancingGiants.getEntityIds;

public class GiantIdListener implements Listener {
    @EventHandler
    public void onInteractWithGiant(PlayerInteractAtEntityEvent event) {
        if(!(event.getPlayer().isSneaking())) return;

        if(!(((CraftEntity)event.getRightClicked()).getHandle() instanceof Giant) && !(((CraftEntity)event.getRightClicked()).getHandle() instanceof Zombie)) return;

        int id = (((CraftEntity)event.getRightClicked()).getHandle() instanceof Giant ? (Giant) ((CraftEntity)event.getRightClicked()).getHandle() : (Zombie) ((CraftEntity)event.getRightClicked()).getHandle()).getId();

        if(!(getEntityIds().contains(id))) return;

        event.getPlayer().sendMessage(ChatColor.GREEN + "Entity Id: " + id);
    }
}
