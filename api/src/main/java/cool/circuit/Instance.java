package cool.circuit;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class Instance {
    private static JavaPlugin instance;

    public static void setInstance(JavaPlugin newInstance) {
        instance = newInstance;
    }

    public static @Nullable JavaPlugin getInstance() {
        return instance;
    }


}
