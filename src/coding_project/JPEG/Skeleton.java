package coding_project.JPEG;

import java.util.Map;

public class Skeleton extends Entity implements Monster {
	public Skeleton() {
		super(10, 2, "Skeleton");
		setSprite(SpriteLoader.load("entities/skeleton.png"));
	}

	public Skeleton(int hp, int damage, double x_speed, double y_speed) {
		super(hp, damage, "Skeleton",  x_speed, y_speed);
		setSprite(SpriteLoader.load("entities/skeleton.png"));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ARROW, new DropRule(0.5, 0, 5),
                DroppableItems.BONE, new DropRule(0.6, 0, 5),
                DroppableItems.XP, new DropRule(0.9, 1,5)
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