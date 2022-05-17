import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener implements Listener {
    private Bingo plugin = Bingo.plugin;

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (EntityType.PLAYER == e.getEntityType()) {
            if (e.getItem().getItemStack().equals(Bingo.bingoCard))
                e.setCancelled(true);
            for (ItemStack is : Bingo.bingoItems) {
                if (is.getType().equals(e.getItem().getItemStack().getType()) && this.plugin.getBingoPlayer((Player)e.getEntity()).checkItem(is))
                    this.plugin.getBingoPlayer((Player)e.getEntity()).gotItem(is);
            }
        }
    }
} 