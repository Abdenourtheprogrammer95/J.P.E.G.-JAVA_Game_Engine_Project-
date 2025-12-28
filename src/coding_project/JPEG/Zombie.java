package coding_project.JPEG;

import java.util.Map;

public class Zombie extends Entity implements Monster {
	public Zombie() {
		super(10, 3, "Zombie", 0.92, 0.92);
		setSprite(SpriteLoader.load("entities/zombie.png"));
	}

	public Zombie(int hp, int damage, double x_speed, double y_speed) {
		super(hp, damage, "Zombie",  x_speed, y_speed);
		setSprite(SpriteLoader.load("entities/zombie.png"));
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

	@Override
	public EnemyState getState() {
		return null;
	}

	@Override
	public void setState(EnemyState getState) {

	}
}