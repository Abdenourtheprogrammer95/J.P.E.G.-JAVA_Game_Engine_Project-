package coding_project.JPEG;

import java.awt.image.BufferedImage;

public interface Item {
    void onAcquire(Player player, int amount);
    BufferedImage getSprite();
}