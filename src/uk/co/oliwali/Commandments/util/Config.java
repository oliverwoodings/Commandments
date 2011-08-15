package uk.co.oliwali.Commandments.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.util.config.Configuration;

import uk.co.oliwali.Commandments.EventType;
import uk.co.oliwali.Commandments.Commandments;
import uk.co.oliwali.Commandments.Rule;

/**
 * Configuration manager for DataLog.
 * Any field with the first letter capitalised is a config option
 * @author oliverw92
 */
public class Config {
	
	public static List<Rule> Rules = new ArrayList<Rule>();
	
	private Configuration config;
	
	/**
	 * Loads the config from file and validates the data
	 * @param plugin
	 */
	public Config (Commandments plugin) {
		
		config = plugin.getConfiguration();
		List<String> keys = config.getKeys(null);
		
		//If there is no config file
		if (keys.size() ==  0) {
			Util.info("No config.yml detected, creating default file");
			keys = new ArrayList<String>();
		}
		
		//Check general settings
		if (!keys.contains("rules")) {
			config.setProperty("rules.fireblock.events", Arrays.asList(new String[]{"block-place"}));
			config.setProperty("rules.fireblock.pattern", "\\b51\\b");
			config.setProperty("rules.fireblock.worlds", Arrays.asList(new String[]{"pvp"}));
			config.setProperty("rules.fireblock.notify-message", "%PLAYER% placed illegal fire block on %WORLD%");
			config.setProperty("rules.fireblock.warn-message", "You are not allowed to place illegal fire blocks on %WORLD%!");
			config.setProperty("rules.fireblock.action.notify", true);
			config.setProperty("rules.fireblock.action.warn", true);
			config.setProperty("rules.fireblock.action.kick", true);
			config.setProperty("rules.fireblock.action.deny", true);
			config.setProperty("rules.fireblock.exclude-groups", Arrays.asList(new String[]{"admins"}));
		}
		
		//Attempt a save
		if (!config.save())
			Util.severe("Error while writing to config.yml");
		
		//Load rules
		keys = config.getKeys("rules");
		outer:
		for (String name : keys) {
			List<EventType> events = new ArrayList<EventType>();
			for (String event : config.getStringList("rules." + name + ".events", null)) {
				if (EventType.fromName(event) == null) {
					Util.severe("Invalid event name found in rule '" + name + "': " + event);
					continue outer;
				}
				events.add(EventType.fromName(event));
			}
			if (events.size() == 0) {
				Util.severe("No valid events supplied in rule '" + name + "'");
				continue outer;
			}
			Rule rule = new Rule(name, events, config.getString("rules." + name + ".pattern", ""), config.getStringList("rules." + name + ".worlds", null), config.getStringList("rules." + name + ".exclude-groups", null), config.getString("rules." + name + ".notify-message", ""), config.getString("rules." + name + ".warn-message", ""), config.getBoolean("rules." + name + ".action.notify", false), config.getBoolean("rules." + name + ".action.warn", false), config.getBoolean("rules." + name + ".action.kick", false), config.getBoolean("rules." + name + ".action.deny", false));
			Rules.add(rule);
		}
		Util.info(Rules.size() + " rule(s) out of " + keys.size() + " loaded from config file");
	}
	
}