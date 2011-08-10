package uk.co.oliwali.Commandments;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration class representing all the different actions that Commandments can handle
 * @author oliverw92
 */
public enum ActionType {
	
	BLOCK_BREAK("block-break"),
	BLOCK_PLACE("block-place"),
	SIGN_PLACE("sign-place"),
	CHAT("chat"),
	COMMAND("command"),
	JOIN("join"),
	QUIT("quit"),
    TELEPORT("teleport"),
    LAVA_BUCKET("lava-bucket"),
    WATER_BUCKET("water-bucket"),
    OPEN_CHEST("open-chest"),
    DOOR_INTERACT("door-interact"),
    PVP_DEATH("pvp-death"),
	FLINT_AND_STEEL("flint-steel"),
	LEVER("lever"),
	STONE_BUTTON("button"),
	EXPLOSION("explosion"),
	BLOCK_BURN("block-burn"),
	SNOW_FORM("snow-form"),
	ICE_FORM("ice-form"),
	SNOW_MELT("snow-melt"),
	ICE_MELT("ice-melt"),
	LEAF_DECAY("leaf-decay"),
	ITEM_DROP("item-drop"),
	ITEM_PICKUP("item-pickup"),
	LAVA_FLOW("lava-flow"),
	WATER_FLOW("water-flow"),
	KICK("kick");
	
	private String name;
	
	private static final Map<String, ActionType> nameMapping = new HashMap<String, ActionType>();
	
	static {
		//Mapping to enable quick finding of ActionTypes by name
		for (ActionType type : EnumSet.allOf(ActionType.class)) {
			nameMapping.put(type.name, type);
		}
	}
	
	private ActionType(String name) {
		this.name = name;
	}
	
	/**
	 * Get the name of the DataType
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get a matching DataType from the supplied config name
	 * @param name DataType config name to search for
	 * @return {@link DataType}
	 */
	public static ActionType fromName(String name) {
		return nameMapping.get(name);
	}

}
