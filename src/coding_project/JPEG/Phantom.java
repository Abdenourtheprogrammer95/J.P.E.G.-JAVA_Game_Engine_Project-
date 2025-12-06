package coding_project.JPEG;

import java.util.Map;

public class Phantom extends Entity {
	public Phantom() {
		super(10, 2, "Phantom");
	}

	public Phantom(int hp, int damage) {
		super(hp, damage, "Phantom");
	}

    /* @Override
    public void canDrop() {
        this.setDropped("a membrane");
        super.canDrop();
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.PHANTOM_MEMBRANE, new DropRule(0.9, 1, 3),
                DroppableItems.XP, new DropRule(0.9, 1, 3)
                );
    }
}