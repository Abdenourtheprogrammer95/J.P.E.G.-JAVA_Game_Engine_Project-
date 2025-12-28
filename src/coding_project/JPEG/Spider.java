package coding_project.JPEG;

import java.util.Map;

public class Spider extends Entity implements Monster {
	public Spider() {
		super(8, 2, "Spider", 1.4, 1.4);
		setSprite(SpriteLoader.load("entities/spider.png"));
	}

	public Spider(int hp, int damage, double x_speed, double y_speed) {
		super(hp, damage, "Spider",  x_speed, y_speed);
		setSprite(SpriteLoader.load("entities/spider.png"));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.SPIDER_EYE, new DropRule(1.0/3.0, 0, 1),
                DroppableItems.STRING, new DropRule(0.6, 0, 5),
                DroppableItems.XP, new DropRule(0.9, 1, 5)
        );
    }

	@Override
	public EnemyState getState() {
		return null;
	}

	@Override
	public void setState(EnemyState getState) {

	}
}