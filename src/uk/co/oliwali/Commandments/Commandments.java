package uk.co.oliwali.Commandments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.oliwali.Commandments.listeners.CommandmentsBlockListener;
import uk.co.oliwali.Commandments.listeners.CommandmentsEntityListener;
import uk.co.oliwali.Commandments.listeners.CommandmentsPlayerListener;
import uk.co.oliwali.Commandments.util.BlockUtil;
import uk.co.oliwali.Commandments.util.Config;
import uk.co.oliwali.Commandments.util.Permission;
import uk.co.oliwali.Commandments.util.Util;

public class Commandments extends JavaPlugin {
	
	public String name;
	public String version;
	public Config config;
	public CommandmentsBlockListener blockListener = new CommandmentsBlockListener();
	public CommandmentsEntityListener entityListener = new CommandmentsEntityListener();
	public CommandmentsPlayerListener playerListener = new CommandmentsPlayerListener();
	
	/**
	 * Shuts down Commandments
	 */
	public void onDisable() {
		Util.info("Version " + version + " disabled!");
	}
	
	/**
	 * Starts up DataLog initiation process
	 */
	public void onEnable() {
		
		Util.info("Starting DataLog initiation process...");

		//Set up config and permissions
        PluginManager pm = getServer().getPluginManager();
		name = this.getDescription().getName();
        version = this.getDescription().getVersion();
        config = new Config(this);
        new Permission(this);
		
        //Register control events
        pm.registerEvent(Type.BLOCK_BREAK, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_PLACE, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_BURN, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.LEAVES_DECAY, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_FORM, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_FROMTO, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.BLOCK_FADE, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.SIGN_CHANGE, blockListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_JOIN, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_QUIT, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_CHAT, playerListener, Event.Priority.Lowest, this);
        pm.registerEvent(Type.PLAYER_TELEPORT, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_DROP_ITEM, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Event.Priority.Highest, this);
        pm.registerEvent(Type.ENTITY_EXPLODE, entityListener, Event.Priority.Highest, this);
        
        Util.info("Version " + version + " enabled!");
        
	}
	
	/**
	 * Checks the event against currently loaded rules
	 * @return true if event is to be cancelled
	 */
	public static boolean checkRules(Player player, ActionType type, Location loc, String data) {
		return checkRules(player.getName(), type, loc, data);
	}
	public static boolean checkRules(String player, ActionType type, Location loc, String data) {
		
		//Check rules
		for (Rule rule : Config.Rules) {
			
			String matchText = "";
			String notification = rule.notificationMsg;
			String warning = rule.warningMsg;
			
			//Check events and worlds
			if (!rule.events.contains(type)) continue;
			if (rule.worlds != null && rule.worlds.size() > 0 && !rule.worlds.contains(loc.getWorld().getName())) continue;
			
			//Check groups
			boolean inGroup = false;
			for (String group : rule.excludeGroups)
				if (Permission.inSingleGroup(loc.getWorld().getName(), player, group)) inGroup = true;
			if (inGroup) continue;
			
			//Check pattern
			if (!rule.pattern.equals("")) {
				Pattern pattern = Pattern.compile(rule.pattern, Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(data);
				if (!matcher.find()) continue;
				matchText = data.substring(matcher.start(), matcher.end());
			}
			
			//Replace text
			notification = notification.replaceAll("%PLAYER%", player);
			notification = notification.replaceAll("%WORLD%", loc.getWorld().getName());
			notification = notification.replaceAll("%MATCH%", matchText);
			warning = warning.replaceAll("%PLAYER%", player);
			warning = warning.replaceAll("%WORLD%", loc.getWorld().getName());
			warning = warning.replaceAll("%MATCH%", matchText);
			
			//Replace match text for certain items
			switch (type) {
				case BLOCK_BREAK:
				case BLOCK_PLACE:
				case ITEM_DROP:
				case ITEM_PICKUP:
					matchText = BlockUtil.getBlockStringName(matchText);
					break;
			}
			
			//Execute actions
			if (rule.notify) {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (Permission.notify(p))
						Util.sendMessage(p, notification);
				}
			}
			Player offender = Bukkit.getServer().getPlayer(player);
			if (offender != null) {
				if (rule.kick)
					offender.kickPlayer(warning);
				else if (rule.warn)
					Util.sendMessage(offender, warning);
			}
			if (rule.deny)
				return true;
				
		}
		
		return false;
	}

}
