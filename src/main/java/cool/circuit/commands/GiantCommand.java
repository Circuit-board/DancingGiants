package cool.circuit.commands;

import cool.circuit.mob.DancingGiant;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Giant;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.entity.Player;

import java.util.List;

import static cool.circuit.DancingGiants.getEntityIds;

public class GiantCommand implements TabExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        if(!(sender instanceof final Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if(!player.hasPermission("dancinggiants.command")) {
            player.sendMessage(ChatColor.RED + "[!] You do not have permission to use this command!");
            return false;
        }

        if(args.length == 0) {
            Giant giant = new DancingGiant(player.getLocation());
            player.sendMessage(ChatColor.GREEN + "[!] Successfully created giant! Use this to specify the giant in other commands: " + giant.getId());
            return true;
        }

        if(args.length == 1 && args[0].equals("help")) {
            player.sendMessage("| Dancing Giants");
            player.sendMessage("| Commands:                      ");
            player.sendMessage("| /giant -> Creates a giant      ");
            player.sendMessage("| /giant help -> Shows this msg  ");
            player.sendMessage("| /giant kill [id] -> Kills a giant with that specified id");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("kill")) {
            try {

                int id = Integer.parseInt(args[1]);
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().getEntity(id);

                if(!entity.isAlive()) {
                    player.sendMessage(ChatColor.RED + "Giant is already dead!");
                    return false;
                }

                if (entity == null) {
                    player.sendMessage(ChatColor.RED + "No entity found with ID " + id);
                    return false;
                }

                if (!(entity instanceof final Giant giant)) {
                    player.sendMessage(ChatColor.RED + "Entity with ID " + id + " is not a Giant.");
                    return false;
                }

                giant.kill(((CraftWorld) player.getWorld()).getHandle());
                player.sendMessage(ChatColor.GREEN + "[!] Successfully removed Giant with ID " + id + "!");
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid ID. Please enter a number.");
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String s, final String[] args) {
        if(args.length == 1) return List.of("help", "kill");

        if(args.length == 2) {
            return getEntityIds().stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }
}
