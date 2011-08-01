package uk.co.oliwali.Commandments.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;

import uk.co.oliwali.Commandments.ActionType;
import uk.co.oliwali.Commandments.Commandments;

public class CommandmentsEntityListener extends EntityListener {

	public void onEntityExplode(EntityExplodeEvent event) {
		for (Block b : event.blockList().toArray(new Block[0]))
			if (Commandments.checkRules("Environment", ActionType.EXPLOSION, b.getLocation(), Integer.toString(b.getTypeId()))) {
				event.setCancelled(true);
				return;
			}
	}
	
}
