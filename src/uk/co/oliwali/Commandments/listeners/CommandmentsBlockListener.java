package uk.co.oliwali.Commandments.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;

import uk.co.oliwali.Commandments.Commandments;
import uk.co.oliwali.Commandments.EventType;
import uk.co.oliwali.Commandments.util.BlockUtil;

public class CommandmentsBlockListener extends BlockListener {
	
	public void onBlockBreak(BlockBreakEvent event) {
		Block block   = event.getBlock();	
		if (Commandments.checkRules(event.getPlayer(), EventType.BLOCK_BREAK, block.getLocation(), BlockUtil.getBlockString(block)))
			event.setCancelled(true);
	}
	
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block   = event.getBlock();
		if (Commandments.checkRules(event.getPlayer(), EventType.BLOCK_PLACE, block.getLocation(), BlockUtil.getBlockString(block)))
			event.setCancelled(true);
	}
	
	public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
    	Location loc  = event.getBlock().getLocation();
        String text = "";
        for (String line : event.getLines())
            text = text + "|" + line;
        if (Commandments.checkRules(player, EventType.SIGN_PLACE, loc, text))
        	event.setCancelled(true);
	}
	
	public void onBlockForm(BlockFormEvent event) {
		switch (event.getNewState().getTypeId()) {
			case 79:
				if (Commandments.checkRules("Environment", EventType.ICE_FORM, event.getBlock().getLocation(), "0"))
					event.setCancelled(true);
				break;
			case 78:
				if (Commandments.checkRules("Environment", EventType.SNOW_FORM, event.getBlock().getLocation(), "0"))
					event.setCancelled(true);
				break;
		}
	}
	
	public void onBlockFade(BlockFadeEvent event) {
		switch (event.getBlock().getTypeId()) {
			case 79:
				if (Commandments.checkRules("Environment", EventType.ICE_MELT, event.getBlock().getLocation(), "0"))
					event.setCancelled(true);
				break;
			case 78:
				if (Commandments.checkRules("Environment", EventType.SNOW_MELT, event.getBlock().getLocation(), "0"))
					event.setCancelled(true);
				break;
		}
	}
	
	public void onBlockFromTo(BlockFromToEvent event) {
		int from = event.getBlock().getTypeId();
		int to = event.getToBlock().getTypeId();
		if (from == 10 || from == 11)
			if (Commandments.checkRules("Environment", EventType.LAVA_FLOW, event.getBlock().getLocation(), Integer.toString(to)))
				event.setCancelled(true);
		if (from == 8 || from == 9)
			if (Commandments.checkRules("Environment", EventType.WATER_FLOW, event.getBlock().getLocation(), Integer.toString(to)))
				event.setCancelled(true);
	}
	
	public void onBlockBurn(BlockBurnEvent event) {
		if (Commandments.checkRules("Environment", EventType.BLOCK_BURN, event.getBlock().getLocation(), Integer.toString(event.getBlock().getTypeId())))
			event.setCancelled(true);
	}
	
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (Commandments.checkRules("Environment", EventType.LEAF_DECAY, event.getBlock().getLocation(), "18"))
			event.setCancelled(true);
	}

}
