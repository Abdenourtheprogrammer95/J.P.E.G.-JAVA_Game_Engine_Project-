package coding_project.JPEG;

import java.awt.*;

public class DroppedItem {

    private final Item item;
    private final int amount;
    private float x, y;
    private boolean remove; // no need to initialize "remove"

    public DroppedItem(Item item, int amount, float x, float y) {
        this.item = item;
        this.amount = amount;
        this.x = x;
        this.y = y;
    }

    private boolean isPlayerClose(Player p) {
        return Math.abs(p.getXpos() - x) < 16 &&
                Math.abs(p.getYpos() - y) < 16;
    }

    public void update(Player player) {
        if (isPlayerClose(player)) {
            item.onAcquire(player, amount);
            markForRemoval();
        }
    }

    private void markForRemoval() {
        remove = true;
    }

    public void render(Graphics2D g) {
        g.drawImage(item.getSprite(), (int) x, (int) y, null);
    }
}