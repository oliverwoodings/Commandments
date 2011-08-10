package uk.co.oliwali.Commandments.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import uk.co.oliwali.Commandments.Commandments;
import uk.co.oliwali.Commandments.ActionType;

public class CommandmentsPlayerListener extends PlayerListener {
	
	public void onPlayerChat(PlayerChatEvent event) {
		if (Commandments.checkRules(event.getPlayer(), ActionType.CHAT, event.getPlayer().getLocation(), event.getMessage()))
			event.setCancelled(true);
	}
	
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		Location loc  = player.getLocation();
		if (Commandments.checkRules(player, ActionType.COMMAND, loc, event.getMessage()))
			event.setCancelled(true);
	}
	
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Location from = event.getFrom();
		Location to   = event.getTo();
		if (Commandments.checkRules(event.getPlayer(), ActionType.TELEPORT, from, to.getWorld().getName() + ": " + to.getX() + ", " + to.getY() + ", " + to.getZ()))
			event.setCancelled(true);
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (Commandments.checkRules(event.getPlayer(), ActionType.JOIN, event.getPlayer().getLocation(), event.getPlayer().getName()))
			event.getPlayer().kickPlayer("You have been denied from this server!");
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Commandments.checkRules(event.getPlayer(), ActionType.QUIT, event.getPlayer().getLocation(), event.getPlayer().getName());
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		if (Commandments.checkRules(event.getPlayer(), ActionType.KICK, event.getPlayer().getLocation(), event.getReason()))
			event.setCancelled(true);
	}
	
	/**
	 * Handles several actions: 
	 * OPEN_CHEST, DOOR_INTERACT, LEVER, STONE_BUTTON, FLINT_AND_STEEL, LAVA_BUCKET, WATER_BUCKET
	 */
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (block != null) {
			
			Location loc = block.getLocation();
	
			switch (block.getType()) {
				case CHEST:
					if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Commandments.checkRules(player, ActionType.OPEN_CHEST, loc, ""))
						event.setCancelled(true);
					break;
				case WOODEN_DOOR:
					if (Commandments.checkRules(player, ActionType.DOOR_INTERACT, loc, ""))
						event.setCancelled(true);
					break;
				case LEVER:
					if (Commandments.checkRules(player, ActionType.LEVER, loc, ""))
						event.setCancelled(true);
					break;
				case STONE_BUTTON:
					if (Commandments.checkRules(player, ActionType.STONE_BUTTON, loc, ""))
						event.setCancelled(true);
					break;
			}
			
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				switch (player.getItemInHand().getType()) {
					case FLINT_AND_STEEL:
						if (Commandments.checkRules(player, ActionType.FLINT_AND_STEEL, loc, ""))
							event.setCancelled(true);
						break;
					case LAVA_BUCKET:
						if (Commandments.checkRules(player, ActionType.LAVA_BUCKET, loc, ""))
							event.setCancelled(true);
						break;
					case WATER_BUCKET:
						if (Commandments.checkRules(player, ActionType.WATER_BUCKET, loc, ""))
							event.setCancelled(true);
						break;
				}
			}
		
		}
		
	}

}
