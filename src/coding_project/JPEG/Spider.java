package coding_project.JPEG;

import java.util.Map;

public class Spider extends Entity {
	public Spider() {
		super(8, 2, "Spider");
	}

	public Spider(int hp, int damage) {
		super(hp, damage, "Spider");
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.SPIDER_EYE, new DropRule(1.0/3.0, 0, 1),
                DroppableItems.STRING, new DropRule(0.6, 0, 5),
                DroppableItems.XP, new DropRule(0.9, 1, 5)
        );
    }
}