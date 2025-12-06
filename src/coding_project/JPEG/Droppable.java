package coding_project.JPEG;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public interface Droppable {
    /* List<String> droppable = new ArrayList<>();
    Collections.addAll(droppable, "xp", "enderpearl", "gunpowder", "bone", "string", "arrow", "rotten flesh",
            "potato", "carrot", "iron ingot", "phantom membrane", "spider eye"); */

    Map<Item, Integer> generateDrops(); // bodiless dictionary implementation method
	/* public void canDrop(); */
}