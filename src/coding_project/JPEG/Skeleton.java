package coding_project.JPEG;

import java.util.Map;

public class Skeleton extends Entity {
	public Skeleton() {
		super(10, 2, "Skeleton");
	}

	public Skeleton(int hp, int damage) {
		super(hp, damage, "Skeleton");
	}

    /* @Override
    public void canDrop() {
        this.setDropped("either a bone, an arrow, or both");
        super.canDrop();
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ARROW, new DropRule(0.9, 1, 3),
                DroppableItems.BONE, new DropRule(0.9, 1, 3),
                DroppableItems.XP, new DropRule(0.9, 1, 3)
                );
    }
}