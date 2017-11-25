package com.blendycat.quadvotes.sql;

import com.blendycat.quadvotes.VotePlugin;
import com.vexsoftware.votifier.model.Vote;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;

import static com.blendycat.bukkit.utils.DatabaseManager.getConnection;

/**
 * Created by EvanMerz on 11/9/17.
 */
public class QueryManager {

    public static void setUpTables(){
        Connection conn = getConnection();
        try{
            CallableStatement stmt = conn.prepareCall(
                    "CREATE TABLE IF NOT EXISTS `vote_links`(" +
                            "`descriptor` VARCHAR(255) NOT NULL, " +
                            "`link` VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY(`descriptor`));"
            );
            stmt.execute();
            stmt = conn.prepareCall(
                    "CREATE TABLE IF NOT EXISTS `player_votes`(" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`uuid` VARCHAR(255) NOT NULL, " +
                            "`link` VARCHAR(255) NOT NULL, " +
                            "`timestamp` VARCHAR(255) NOT NULL, " +
                            "PRIMARY KEY(`id`));"
            );
            stmt.execute();

            conn.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void addLink(String descriptor, String link){
        Connection conn = getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO `vote_links`(`descriptor`, `link`) " +
                            "VALUES(?, ?);"
            );
            stmt.setString(1, descriptor);
            stmt.setString(2, link);
            stmt.execute();
            VotePlugin.addLink(descriptor, link);
            // Finally close the connection
            conn.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void removeLink(String descriptor){
        Connection conn = getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM `vote_links` WHERE `descriptor`=?;"
            );
            // Set the parameter to the id
            stmt.setString(1, descriptor);
            // Execute the query
            stmt.execute();
            // Remove the link from the cache
            VotePlugin.removeLink(descriptor);
            // Finally close the connection
            conn.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public static HashMap<String, String> getLinks(){
        Connection conn = getConnection();
        HashMap<String, String> links = new HashMap<>();
        try{
            CallableStatement stmt = conn.prepareCall(
                    "SELECT * FROM `vote_links`;"
            );
            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            while(rs.next()){
                links.put(rs.getString(1), rs.getString(2));
            }
            conn.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return links;
    }

    public static void addVote(Player voter, Vote vote){
        String uuid = voter.getUniqueId().toString();
        String timeStamp = vote.getTimeStamp();
        Connection conn = getConnection();
        try{
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO `player_votes`(`uuid`, `link`, `timestamp`) " +
                            "VALUES(?, ?, ?);"
            );
            stmt.setString(1, uuid);
            stmt.setString(2, vote.getServiceName());
            stmt.setString(3, timeStamp);
            stmt.execute();
            conn.close();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
