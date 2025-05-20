package cool.circuit;

import cool.circuit.goal.BabyGoal_v1_21_R1;
import cool.circuit.goal.BabyGrowGoal_v1_21_R1;
import cool.circuit.goal.DanceGoal_v1_21_R1;
import cool.circuit.goal.SwingGoal_v1_21_R1;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.ai.goal.PathfinderGoalMeleeAttack;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomLookaround;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomStroll;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.item.ItemStack;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.circuit.Variables.ENTITY_IDS;
import static cool.circuit.Variables.GOALS;

public class GiantCommand_v1_21_R1 implements TabExecutor {

    public static final List<Map<EnumItemSlot, ItemStack>> clothes = new ArrayList<>();
    private final Map<EntityGiantZombie, Boolean> hostiles = new HashMap<>();
    private static final Map<EntityGiantZombie, Boolean> swings = new HashMap<>();

    public GiantCommand_v1_21_R1() {
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

            Map<EnumItemSlot, ItemStack> map = new HashMap<>();
            map.put(EnumItemSlot.f, CraftItemStack.asNMSCopy(helmet));
            map.put(EnumItemSlot.e, CraftItemStack.asNMSCopy(chestplate));
            map.put(EnumItemSlot.d, CraftItemStack.asNMSCopy(leggings));
            map.put(EnumItemSlot.c, CraftItemStack.asNMSCopy(boots));

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
            return ENTITY_IDS.stream()
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }

    private boolean handleArgs(Player player, String[] args) {
        if (args.length == 0) {

            if (Math.random() > 0.5) {

                EntityGiantZombie giant = new EntityGiantZombie(EntityTypes.U, ((CraftWorld) player.getWorld()).getHandle());
                giant.a_(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

                SwingGoal_v1_21_R1 swingGoal = new SwingGoal_v1_21_R1(giant);
                swingGoal.setSwinging(false);
                GOALS.put(giant, swingGoal);

                hostiles.put(giant, false);
                swings.put(giant, false);

                int clothesIndex = (int) (Math.random() * clothes.size());

                for (Map.Entry<EnumItemSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                    giant.setItemSlot(entry.getKey(), entry.getValue(), true);
                }

                giant.bW.b().clear();

                giant.bW.a(0, new DanceGoal_v1_21_R1(giant));
                giant.bW.a(1, new PathfinderGoalRandomLookaround(giant));
                giant.bW.a(2, new PathfinderGoalRandomStroll(giant, 1.0));

                ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(giant, CreatureSpawnEvent.SpawnReason.COMMAND);

                ENTITY_IDS.add(giant.an());

                player.sendMessage(ChatColor.GREEN + "[!] Successfully created giant! Use this to specify the giant in other commands: " + giant.an());
                return true;
            } else {
                EntityZombie zombie = new EntityZombie(EntityTypes.bu, ((CraftWorld) player.getWorld()).getHandle());
                zombie.a_(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

                int clothesIndex = (int) (Math.random() * clothes.size());

                for (Map.Entry<EnumItemSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                    zombie.setItemSlot(entry.getKey(), entry.getValue(), true);
                }

                zombie.bW.b().clear();

                zombie.bW.a(0, new DanceGoal_v1_21_R1(zombie));
                zombie.bW.a(1, new PathfinderGoalRandomLookaround(zombie));
                zombie.bW.a(2, new PathfinderGoalRandomStroll(zombie, 1.0));
                zombie.bW.a(3, new BabyGrowGoal_v1_21_R1(zombie));

                ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(zombie, CreatureSpawnEvent.SpawnReason.COMMAND);

                ENTITY_IDS.add(zombie.an());
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
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().a(id);

                if (entity != null && !entity.bE()) {
                    player.sendMessage(ChatColor.RED + "Giant is already dead!");
                    return false;
                }

                if (entity == null) return false;

                entity.ap();
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
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().a(id);

                if (!(entity instanceof EntityGiantZombie giant)) {
                    player.sendMessage(ChatColor.RED + "No Dancing Giant with ID " + id + " found.");
                    return false;
                }

                if (!giant.bE()) {
                    player.sendMessage(ChatColor.RED + "No alive Dancing Giant with ID " + id + " found.");
                    return false;
                }

                boolean isHostile = hostiles.getOrDefault(giant, false);
                if (isHostile) {
                    giant.bW.a(g -> g instanceof PathfinderGoalMeleeAttack);
                    giant.bX.a(g -> g instanceof PathfinderGoalNearestAttackableTarget<?>);

                    hostiles.put(giant, false);
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is no longer hostile.");
                } else {
                    giant.bW.a(2, new PathfinderGoalMeleeAttack(giant, 1.0, true));
                    giant.bX.a(0, new PathfinderGoalNearestAttackableTarget<>(giant, net.minecraft.world.entity.player.EntityHuman.class, true));

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
                Entity entity = ((CraftWorld) player.getWorld()).getHandle().a(id);

                if (!(entity instanceof EntityGiantZombie giant)) {
                    player.sendMessage(ChatColor.RED + "No Dancing Giant with ID " + id + " found.");
                    return false;
                }

                if (!giant.bE()) {
                    player.sendMessage(ChatColor.RED + "No alive Dancing Giant with ID " + id + " found.");
                    return false;
                }

                boolean isSwinging = swings.getOrDefault(giant, false);

                if(!GOALS.containsKey(giant)) return false;

                if (isSwinging) {
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is no longer swinging.");
                    GOALS.get(giant).setSwinging(false);
                    swings.put(giant, false);
                } else {
                    player.sendMessage(ChatColor.GREEN + "Giant with ID " + id + " is now swinging.");
                    GOALS.get(giant).setSwinging(true);
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
            EntityGiantZombie adult = new EntityGiantZombie(EntityTypes.U, ((CraftWorld) player.getWorld()).getHandle());
            adult.a_(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

            SwingGoal_v1_21_R1 swingGoal = new SwingGoal_v1_21_R1(adult);
            swingGoal.setSwinging(false);
            GOALS.put(adult, swingGoal);

            hostiles.put(adult, false);
            swings.put(adult, false);

            int clothesIndex = (int) (Math.random() * clothes.size());

            for (Map.Entry<EnumItemSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                adult.setItemSlot(entry.getKey(), entry.getValue(), true);
            }

            adult.bW.b().clear();

            adult.bW.a(0, new DanceGoal_v1_21_R1(adult));
            adult.bW.a(1, new PathfinderGoalRandomLookaround(adult));
            adult.bW.a(2, new PathfinderGoalRandomStroll(adult, 1.0));

            ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(adult, CreatureSpawnEvent.SpawnReason.COMMAND);

            ENTITY_IDS.add(adult.an());

            //Spawn baby
            EntityZombie baby = new EntityZombie(EntityTypes.bu, ((CraftWorld) player.getWorld()).getHandle());
            baby.a_(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());

            for (Map.Entry<EnumItemSlot, ItemStack> entry : clothes.get(clothesIndex).entrySet()) {
                baby.setItemSlot(entry.getKey(), entry.getValue(), true);
            }

            baby.bW.b().clear();

            baby.bW.a(0, new DanceGoal_v1_21_R1(baby));
            baby.bW.a(1, new PathfinderGoalRandomLookaround(baby));
            baby.bW.a(2, new BabyGoal_v1_21_R1(adult, baby));

            ((CraftWorld) player.getWorld()).getHandle().addFreshEntity(baby, CreatureSpawnEvent.SpawnReason.COMMAND);

            ENTITY_IDS.add(baby.an());
        }

        return true;
    }

    public static Map<EntityGiantZombie, Boolean> getSwings() {
        return swings;
    }
}
