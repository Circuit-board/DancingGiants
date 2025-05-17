package cool.circuit;

import cool.circuit.commands.GiantCommand;
import cool.circuit.goal.SwingGoal;
import cool.circuit.listener.DeathListener;
import cool.circuit.listener.GiantIdListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Giant;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_21_R4.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class DancingGiants extends JavaPlugin {

    private static final List<Integer> entityIds = new ArrayList<>();
    private static final Map<Giant, SwingGoal> goals = new HashMap<>();
    private static DancingGiants instance;

    public static List<Integer> getEntityIds() {
        return entityIds;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        entityIds.addAll(getConfig().getIntegerList("ids"));

        // Restore goals
        for (String key : getConfig().getKeys(false)) {
            int id;
            try {
                id = Integer.parseInt(key);
            } catch (NumberFormatException ignored) {
                continue;
            }

            Giant giant = null;

            // Look for the entity in all worlds
            for (World world : Bukkit.getWorlds()) {
                Entity entity = ((CraftWorld) world).getHandle().getEntity(id);
                if (entity instanceof Giant g) {
                    giant = g;
                    break;
                }
            }

            if (giant == null) continue;

            SwingGoal goal = new SwingGoal(giant);
            goal.setSwinging(getConfig().getBoolean(key + ".swinging", false));
            goals.put(giant, goal);
        }

        if (getCommand("giant") != null) {
            getCommand("giant").setExecutor(new GiantCommand());
        }

        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new GiantIdListener(), this);
    }

    @Override
    public void onDisable() {
        FileConfiguration config = getConfig();
        config.set("ids", entityIds);

        for (Map.Entry<Giant, SwingGoal> entry : goals.entrySet()) {
            int id = entry.getKey().getId();
            config.set(id + ".swinging", entry.getValue().isSwinging());
        }

        saveConfig();
    }

    public static DancingGiants getInstance() {
        return instance;
    }

    public static Map<Giant, SwingGoal> getGoals() {
        return goals;
    }
}
