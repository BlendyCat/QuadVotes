package com.blendycat.quadvotes.command;

import com.blendycat.quadvotes.VotePlugin;
import com.blendycat.quadvotes.sql.QueryManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by EvanMerz on 11/10/17.
 */
public class VoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                player.sendMessage(ChatColor.AQUA + "+---------+ Vote Links +---------+");
                boolean alternate = false;
                for(String descriptor : VotePlugin.getDescriptors()){
                    player.sendMessage(ChatColor.GREEN + descriptor+":");
                    String color = alternate ? ChatColor.BLUE + "" : ChatColor.DARK_BLUE + "";
                    alternate = !alternate;
                    player.sendMessage(color + VotePlugin.getLink(descriptor));
                }
            }else if(args.length == 3 && args[0].equalsIgnoreCase("addlink")
                    && player.hasPermission("quadvotes.addlink")){
                String descriptor = args[1];
                String link = args[2];
                QueryManager.addLink(descriptor, link.trim().toLowerCase());
                player.sendMessage(
                        ChatColor.GRAY + "Added " + ChatColor.AQUA + link + ChatColor.GRAY +
                                " to the link list!"
                );
            }
        }else{
            sender.sendMessage(ChatColor.DARK_RED + "That command is only for players!");
        }
        return true;
    }
}
