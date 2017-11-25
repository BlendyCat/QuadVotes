package com.blendycat.quadvotes.listener;

import com.blendycat.quadvotes.VotePlugin;
import com.blendycat.quadvotes.sql.QueryManager;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Created by EvanMerz on 11/9/17.
 */
public class VoteListener implements Listener {

    @EventHandler
    public void onVote(VotifierEvent e){
        // Get the vote object
        Vote vote = e.getVote();
        // Get the username of the voter
        String username = vote.getUsername().trim();
        // We need to find the player with the corresponding username
        Player player = null;
        // Loop through the online players and find any with that username
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getName().equalsIgnoreCase(username)){
                player = p;
                break;
            }
        }

        if(player != null){
            player.sendMessage(ChatColor.GRAY + "Thank you for voting on " +
                    ChatColor.AQUA + vote.getServiceName() +
                    ChatColor.GRAY + "!"
            );
            player.sendMessage(ChatColor.GREEN + "You have been rewarded $30!");
            Economy eco = VotePlugin.getEco();
            eco.depositPlayer(player, 30.0);
            QueryManager.addVote(player, vote);
        }
    }
}
