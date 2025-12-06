package coding_project.JPEG;

import java.util.Map;

public class Zombie extends Entity {
	public Zombie() {
		super(10, 3, "Zombie");
	}

	public Zombie(int hp, int damage) {
		super(hp, damage, "Zombie");
	}

    /* @Override
    public void canDrop() {
        this.setDropped("rotten flesh, a potato/carrot or even an iron ingot");
        super.canDrop();
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ROTTEN_FLESH, new DropRule(0.9, 1, 3),
                DroppableItems.IRON_INGOT, new DropRule(0.9, 1, 3),
                DroppableItems.POTATO, new DropRule(0.9, 1, 3),
                DroppableItems.CARROT, new DropRule(0.9, 1, 3),
                DroppableItems.XP, new DropRule(0.9, 1, 3)
        );
    }
}