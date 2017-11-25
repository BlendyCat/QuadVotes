package com.blendycat.quadvotes;

import com.blendycat.quadvotes.command.VoteCommand;
import com.blendycat.quadvotes.listener.VoteListener;
import com.blendycat.quadvotes.sql.QueryManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by EvanMerz on 11/9/17.
 */
public class VotePlugin extends JavaPlugin {

    private static Economy economy = null;
    private static HashMap<String, String> links;

    @Override
    public void onEnable(){
        setupEconomy();
        Bukkit.getPluginManager().registerEvents(new VoteListener(), this);
        QueryManager.setUpTables();
        links = QueryManager.getLinks();
        getCommand("vote").setExecutor(new VoteCommand());
    }

    public static void removeLink(String descriptor){
        links.remove(descriptor);
    }

    public static void addLink(String descriptor, String link){
        links.put(descriptor, link);
    }

    public static Collection<String> getDescriptors(){
        return links.keySet();
    }

    public static String getLink(String descriptor) {
        return links.get(descriptor);
    }

    public static Economy getEco(){
        return economy;
    }

    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider =
                getServer().getServicesManager()
                        .getRegistration(net.milkbowl
                                .vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
