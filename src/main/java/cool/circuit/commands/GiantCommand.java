package cool.circuit.commands;

import cool.circuit.goal.BabyGoal;
import cool.circuit.goal.BabyGrowGoal;
import cool.circuit.goal.DanceGoal;
import cool.circuit.goal.SwingGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.circuit.DancingGiants.getEntityIds;
import static cool.circuit.DancingGiants.getGoals;

public class GiantCommand implements TabExecutor {

    public static final List<Map<EquipmentSlot, ItemStack>> clothes = new ArrayList<>();
    private final Map<Giant, Boolean> hostiles = new HashMap<>();
    private final Map<Giant, Boolean> swings = new HashMap<>();

    public GiantCommand() {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.AQUA);
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        colors.add(Color.FUCHSIA);
        colors.add(Color.GREEN);
        colors.add(Color.GRAY);
        colors.add(Color.LIME);
        colors.add(Color.MAROON);
        colors.add(Color.NAVY);
        colors.add(Color.ORANGE);
        colors.add(Color.OLIVE);
        colors.add(Color.PURPLE);
        colors.add(Color.RED);
        colors.add(Color.SILVER);
        colors.add(Color.TEAL);
        colors.add(Color.WHITE);
        colors.add(Color.YELLOW);

        for (Color color : colors) {
            org.bukkit.inventory.ItemStack helmet = new org.bukkit.inventory.ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
            if (helmetMeta != null) {
                helmetMeta.setColor(color);
                helmetMeta.setUnbreakable(true);
                helmet.setItemMeta(helmetMeta);
            }

            org.bukkit.inventory.ItemStack chestplate = new org.bukkit.inventory.ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta chestMeta = (LeatherArmorMeta) chestplate.getItemMeta();
            if (chestMeta != null) {
                chestMeta.setColor(color);
                chestMeta.setUnbreakable(true);
                chestplate.setItemMeta(chestMeta);
            }

            org.bukkit.inventory.ItemStack leggings = new org.bukkit.inventory.ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
            if (leggingsMeta != null) {
                leggingsMeta.setColor(color);
                leggingsMeta.setUnbreakable(true);
                leggings.setItemMeta(leggingsMeta);
            }

            org.bukkit.inventory.ItemStack boots = new org.bukkit.inventory.ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
            if (bootsMeta != null) {
                bootsMeta.setColor(color);
                bootsMeta.setUnbreakable(true);
                boots.setItemMeta(bootsMeta);
            }

            Map<EquipmentSlot, ItemStack> map = new HashMap<>();
            map.put(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(helmet));
            map.put(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(chestplate));
            map.put(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(leggings));
            map.put(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(boots));

            clothes.add(map);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return false;
        }

        if (!player.hasPermission("dancinggiants.command")) {
            player.sendMessage(ChatColor.RED + "[!] You do not have permission to use this command!");
            return false;
        }

        return handleArgs(player, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) return List.of("help", "kill", "hostile", "swing", "family");

        if (args.length == 2 && !args[0].equalsIgnoreCase("help")) {
            return getEntityIds().stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }

    private boolean handleArgs(Player player, String[] args) {
        if (args.length == 0) {

            if (Math.random() > 0.5) {

                Giant giant = new Giant(EntityType.GIANT, ((CraftWorld) player.getWorld()).getHandle());
                giant.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

                SwingGoal swingGoal = new SwingGoal(giant);
                swingGoal.setSwinging(false);
                getGoals().put(giant, swingGoal);

                hostiles.put(giant, false);
                swings.put(giant, false);

                int clothesIndex = (int) (Math.random() * clothes.size());

                for (Map.Entry<EquipmentSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                    giant.setItemSlot(entry.getKey(), entry.getValue());
                }

                giant.goalSelector.getAvailableGoals().clear();

                giant.goalSelector.addGoal(0, new DanceGoal(giant));
                giant.goalSelector.addGoal(1, new RandomLookAroundGoal(giant));
                giant.goalSelector.addGoal(2, new RandomStrollGoal(giant, 1.0));

                ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(giant);

                getEntityIds().add(giant.getId());

                player.sendMessage(ChatColor.GREEN + "[!] Successfully created giant! Use this to specify the giant in other commands: " + giant.getId());
                return true;
            } else {
                Zombie zombie = new Zombie(EntityType.ZOMBIE, ((CraftWorld) player.getWorld()).getHandle());
                zombie.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

                int clothesIndex = (int) (Math.random() * clothes.size());

                for (Map.Entry<EquipmentSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                    zombie.setItemSlot(entry.getKey(), entry.getValue());
                }

                zombie.goalSelector.getAvailableGoals().clear();

                zombie.goalSelector.addGoal(0, new DanceGoal(zombie));
                zombie.goalSelector.addGoal(1, new RandomLookAroundGoal(zombie));
                zombie.goalSelector.addGoal(2, new RandomStrollGoal(zombie, 1.0));
                zombie.goalSelector.addGoal(3, new BabyGrowGoal(zombie));

                ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(zombie);

                getEntityIds().add(zombie.getId());
            }
        }

        if (args.length == 1 && args[0].equals("help")) {
            player.sendMessage("| Dancing Giants");
            player.sendMessage("| Commands:                      ");
            player.sendMessage("| /giant -> Creates a giant      ");
            player.sendMessage("| /giant help -> Shows this msg  ");
            player.sendMessage("| /giant kill [id] -> Kills a giant with the specified id");
            player.sendMessage("| /giant hostile [id] -> Toggles if that giant is hostile");
            player.sendMessage("| /giant swing [id] -> Toggles if that giant should swing their arms");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("kill")) {
            try {
                int id = Integer.parseInt(args[1]);
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().getEntity(id);

                if (entity != null && !entity.isAlive()) {
                    player.sendMessage(ChatColor.RED + "Giant is already dead!");
                    return false;
                }

                if (entity == null) return false;

                entity.kill(((CraftWorld) player.getWorld()).getHandle());
                player.sendMessage(ChatColor.GREEN + "[!] Successfully removed Giant with ID " + id + "!");
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid ID. Please enter a number.");
                return false;
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("hostile")) {
            try {
                int id = Integer.parseInt(args[1]);
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().getEntity(id);

                if (!(entity instanceof Giant giant)) {
                    player.sendMessage(ChatColor.RED + "No Dancing Giant with ID " + id + " found.");
                    return false;
                }

                if (!giant.isAlive()) {
                    player.sendMessage(ChatColor.RED + "No alive Dancing Giant with ID " + id + " found.");
                    return false;
                }

                boolean isHostile = hostiles.getOrDefault(giant, false);
                if (isHostile) {
                    giant.goalSelector.removeAllGoals(g -> g instanceof MeleeAttackGoal);
                    giant.targetSelector.removeAllGoals(g -> g instanceof NearestAttackableTargetGoal<?>);

                    hostiles.put(giant, false);
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is no longer hostile.");
                } else {
                    giant.goalSelector.addGoal(2, new MeleeAttackGoal(giant, 1.0, true));
                    giant.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(giant, net.minecraft.world.entity.player.Player.class, true));

                    hostiles.put(giant, true);
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is now hostile.");
                }

                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid ID. Please enter a number.");
                return false;
            }
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("swing")) {
            try {
                int id = Integer.parseInt(args[1]);
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().getEntity(id);

                if (!(entity instanceof Giant giant)) {
                    player.sendMessage(ChatColor.RED + "No Dancing Giant with ID " + id + " found.");
                    return false;
                }

                if (!giant.isAlive()) {
                    player.sendMessage(ChatColor.RED + "No alive Dancing Giant with ID " + id + " found.");
                    return false;
                }

                boolean isSwinging = swings.getOrDefault(giant, false);

                if (isSwinging) {
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is no longer swinging.");
                    getGoals().get(giant).setSwinging(false);
                    swings.put(giant, false);
                } else {
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is now swinging.");
                    getGoals().get(giant).setSwinging(true);
                    swings.put(giant, true);
                }

                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid ID. Please enter a number.");
                return false;
            }
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("family")) {

            //Spawn adult
            Giant adult = new Giant(EntityType.GIANT, ((CraftWorld) player.getWorld()).getHandle());
            adult.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

            SwingGoal swingGoal = new SwingGoal(adult);
            swingGoal.setSwinging(false);
            getGoals().put(adult, swingGoal);

            hostiles.put(adult, false);
            swings.put(adult, false);

            int clothesIndex = (int) (Math.random() * clothes.size());

            for (Map.Entry<EquipmentSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                adult.setItemSlot(entry.getKey(), entry.getValue());
            }

            adult.goalSelector.getAvailableGoals().clear();

            adult.goalSelector.addGoal(0, new DanceGoal(adult));
            adult.goalSelector.addGoal(1, new RandomLookAroundGoal(adult));
            adult.goalSelector.addGoal(2, new RandomStrollGoal(adult, 1.0));

            ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(adult);

            getEntityIds().add(adult.getId());

            //Spawn baby
            Zombie baby = new Zombie(EntityType.ZOMBIE, ((CraftWorld) player.getWorld()).getHandle());
            baby.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

            for (Map.Entry<EquipmentSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                baby.setItemSlot(entry.getKey(), entry.getValue());
            }

            baby.goalSelector.getAvailableGoals().clear();

            baby.goalSelector.addGoal(0, new DanceGoal(baby));
            baby.goalSelector.addGoal(1, new RandomLookAroundGoal(baby));
            baby.goalSelector.addGoal(2, new BabyGoal(adult, baby));

            ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(baby);

            getEntityIds().add(baby.getId());
        }

        return true;
    }
}
