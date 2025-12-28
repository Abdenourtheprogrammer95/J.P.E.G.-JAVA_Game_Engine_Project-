package coding_project.JPEG;

import java.util.Map;

public class Enderman extends Entity implements Monster {
	private EnemyState state;

	public Enderman() {
		super(20, 7, "Enderman", 1.2, 1.2);
		setSprite(SpriteLoader.load("entities/enderman.png"));
	}

	public Enderman(int hp, int damage, double x_speed, double y_speed) {
		super(hp, damage, "Enderman", x_speed, y_speed);
		setSprite(SpriteLoader.load("entities/enderman.png"));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ENDER_PEARL, new DropRule(0.5, 0, 4),
                DroppableItems.XP, new DropRule(0.9, 1, 5)
                );
    }

	@Override
	public void triggerChase(Entity enderman, Player player) {
		System.out.println("Enderman trigger BULLSHIT");
	}

	@Override
	public EnemyState getState() {
		return null;
	}

	@Override
	public void setState(EnemyState getState) {

	}
}