package uk.co.oliwali.Commandments.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;

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
	
	public void onPaintingBreak(PaintingBreakEvent event) {
		if (event.isCancelled()) return;
		Painting painting = event.getPainting();
		if (event.getCause() == RemoveCause.ENTITY) {
			PaintingBreakByEntityEvent e = (PaintingBreakByEntityEvent)event;
			if (e.getRemover() instanceof Player && Commandments.checkRules((Player)e.getRemover(), ActionType.BLOCK_BREAK, painting.getLocation(), "321"))
				event.setCancelled(true);
		}
	}
	
	public void onPaintingPlace(PaintingPlaceEvent event) {
		if (event.isCancelled()) return;
		Painting painting = event.getPainting();
		if (Commandments.checkRules(event.getPlayer(), ActionType.BLOCK_PLACE, painting.getLocation(), "321"))
			event.setCancelled(true);
	}
	
}
