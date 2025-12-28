package coding_project.JPEG;

import java.util.Map;

public class Creeper extends Entity implements Monster {
	public Creeper() {
		super(10, 43, "Creeper");
		setSprite(SpriteLoader.load("entities/creeper.png"));
	}

	public Creeper(int hp, int damage) {
		super(hp, damage, "Creeper");
		setSprite(SpriteLoader.load("entities/creeper.png"));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.GUN_POWDER, new DropRule(0.6667, 0, 5),
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