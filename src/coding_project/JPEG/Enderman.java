package coding_project.JPEG;

import java.util.Map;

public class Enderman extends Entity {
	public Enderman() {
		super(20, 7, "Enderman");
	}

	public Enderman(int hp, int damage) {
		super(hp, damage, "Enderman");
	}

    /* problems for later
    @Override
    public void canDrop() {
        this.setDropped("an enderpearl");
        super.canDrop();
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ENDER_PEARL, new DropRule(0.9, 1, 3),
                DroppableItems.XP, new DropRule(0.9, 1, 3)
                );
    }
}