package cool.circuit;

import cool.circuit.goal.SwingGoal_v1_21_R4;
import cool.circuit.listener.DeathListener_v1_21_R4;
import cool.circuit.listener.GiantIdListener_v1_21_R4;
import cool.circuit.goal.SwingGoal_v1_21_R1;
import cool.circuit.listener.DeathListener_v1_21_R1;
import cool.circuit.listener.GiantIdListener_v1_21_R1;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EntityGiantZombie;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static cool.circuit.Variables.ENTITY_IDS;
import static cool.circuit.Variables.GOALS;

public final class DancingGiants extends JavaPlugin {

    @Override
    public void onEnable() {
        JavaPlugin instance = this;

        saveDefaultConfig();
        ENTITY_IDS.addAll(getConfig().getIntegerList("ids"));

        Instance.setInstance(instance);

        // Restore goals
        for (String key : getConfig().getKeys(false)) {
            int id;
            try {
                id = Integer.parseInt(key);
            } catch (NumberFormatException ignored) {
                continue;
            }

            EntityGiantZombie giant = null;

            // Look for the entity in all worlds
            for (World world : Bukkit.getWorlds()) {
                Entity entity =  (VersionUtils.getVersion().contains("1.21.1") ? CraftWorldGetter_v1_21_R1.craftWorld(world) : CraftWorldGetter_v1_21_R4.craftWorld(world)).a(id);
                if (entity instanceof EntityGiantZombie g) {
                    giant = g;
                    break;
                }
            }

            if (giant == null) continue;

            SwingGoal goal = (VersionUtils.getVersion().contains("1.21.1") ? new SwingGoal_v1_21_R1(giant) : new SwingGoal_v1_21_R4(giant));
            goal.setSwinging(getConfig().getBoolean(key + ".swinging", false));
            GOALS.put(giant, goal);
        }

        if (getCommand("giant") != null) {
            getCommand("giant").setExecutor((VersionUtils.getVersion().contains("1.21.1") ? new GiantCommand_v1_21_R1() : new GiantCommand_v1_21_R4()));
        }

        Bukkit.getPluginManager().registerEvents((VersionUtils.getVersion().contains("1.21.1") ? new GiantIdListener_v1_21_R1() : new GiantIdListener_v1_21_R4()), this);
        Bukkit.getPluginManager().registerEvents((VersionUtils.getVersion().contains("1.21.1") ? new DeathListener_v1_21_R1() : new DeathListener_v1_21_R4()), this);
    }

    @Override
    public void onDisable() {
        FileConfiguration config = getConfig();
        config.set("ids", ENTITY_IDS);

        for (Map.Entry<EntityGiantZombie, SwingGoal> entry : GOALS.entrySet()) {
            int id = (VersionUtils.getVersion().contains("1.21.1") ? EntityIdGetter_v1_21_R1.entityId(entry.getKey()) : EntityIdGetter_v1_21_R4.entityId(entry.getKey()));
            config.set(id + ".swinging", entry.getValue().isSwinging());
        }

        saveConfig();
    }
}
