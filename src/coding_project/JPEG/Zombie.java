package coding_project.JPEG;

import java.util.Map;

public class Zombie extends Entity {
	public Zombie() {
		super(10, 3, "Zombie");
	}

	public Zombie(int hp, int damage) {
		super(hp, damage, "Zombie");
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ROTTEN_FLESH, new DropRule(0.7, 0, 5),
                DroppableItems.IRON_INGOT, new DropRule(0.025, 0, 1),
                DroppableItems.POTATO, new DropRule(0.025, 0, 1),
                DroppableItems.CARROT, new DropRule(0.025, 0, 1),
                DroppableItems.XP, new DropRule(0.9, 1, 5)
        );
    }
}