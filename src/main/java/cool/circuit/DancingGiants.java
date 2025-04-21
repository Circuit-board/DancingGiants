package cool.circuit;

import cool.circuit.commands.GiantCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class DancingGiants extends JavaPlugin {

    private static final List<Integer> entityIds = new ArrayList<>();
    private static DancingGiants instance;

    public static List<Integer> getEntityIds() {
        return entityIds;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        entityIds.addAll(getConfig().getIntegerList("ids"));

        getCommand("giant").setExecutor(new GiantCommand());
    }

    @Override
    public void onDisable() {
        // Save entity IDs to config
        FileConfiguration config = getConfig();
        config.set("ids", entityIds);
        saveConfig();
    }

    public static DancingGiants getInstance() {
        return instance;
    }
}
