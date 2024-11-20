package github.mkaksoy.configurator;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static final Logger logger = new Logger("Configurator");

    @Override
    public void onEnable() {
        logger.log(LogType.INFO, "You're using Configurator v1.0.0.");
    }

}