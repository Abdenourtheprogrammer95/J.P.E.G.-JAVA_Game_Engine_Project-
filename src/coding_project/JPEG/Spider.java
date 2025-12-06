package coding_project.JPEG;

import java.util.Map;

public class Spider extends Entity {
	public Spider() {
		super(8, 2, "Spider");
	}

	public Spider(int hp, int damage) {
		super(hp, damage, "Spider");
	}

    /* @Override
    public void canDrop() {
        this.setDropped("either a string, an eye, or both");
        super.canDrop();
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.SPIDER_EYE, new DropRule(0.9, 1, 3),
                DroppableItems.STRING, new DropRule(0.9, 1, 3),
                DroppableItems.XP, new DropRule(0.9, 1, 3)
        );
    }
}