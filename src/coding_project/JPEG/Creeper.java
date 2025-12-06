package coding_project.JPEG;

import java.util.Map;

public class Creeper extends Entity {
	public Creeper() {
		super(10, 43, "Creeper");
	}

	public Creeper(int hp, int damage) {
		super(hp, damage, "Creeper");
	}

    /* @Override
    public void canDrop() {
        this.setDropped("gunpowder");
        super.canDrop();
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.GUN_POWDER, new DropRule(0.9, 1, 3),
                DroppableItems.XP, new DropRule(0.9, 1, 3)
                );
    }
}