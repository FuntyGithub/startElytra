package org.funty.startelytra;


import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.funty.startelytra.commands.ElytraCommand;
import org.funty.startelytra.listeners.ElytraListener;


public final class Main extends JavaPlugin {

    private static Main plugin;

    public void onEnable() {

        //startup
        plugin = this;
        loadConfig();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ElytraListener(), this);

        getCommand("elytra").setExecutor(new ElytraCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("§4§l[STARTELYTRA] Disabled");
        // Plugin shutdown logic
    }
    public void loadConfig(){
        saveDefaultConfig();
    }
    public static Main getPlugin(){
        return plugin;
    }
}
