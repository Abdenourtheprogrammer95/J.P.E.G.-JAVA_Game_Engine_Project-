package coding_project.JPEG;

import java.util.Map;

public class Phantom extends Entity implements Monster {
	public Phantom() {
		super(10, 2, "Phantom", 1.8, 1.8);
		setSprite(SpriteLoader.load("entities/phantom.png"));
	}

	public Phantom(int hp, int damage, double x_speed, double y_speed) {
		super(hp, damage, "Phantom", x_speed, y_speed);
		setSprite(SpriteLoader.load("entities/phantom.png"));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.PHANTOM_MEMBRANE, new DropRule(0.4, 0, 4),
                DroppableItems.XP, new DropRule(0.9, 1, 5)
                );
    }

	@Override
	public void triggerChase(Entity phantom, Player player) {
		double dx = player.getXpos()-phantom.getXpos(),
				dy = player.getYpos()-phantom.getYpos(),
				distanceToPlayer = Math.sqrt(dx*dx+dy*dy);
		if (distanceToPlayer == 0) return;

		double stepX = (dx/distanceToPlayer) * phantom.getXspeed();
		double stepY = (dy/distanceToPlayer) * phantom.getYspeed();

		// phantom rushes towards the player, with a greater speed than that of other entities
		move(phantom, stepX, stepY, 1.5, 1.5);
	}

	@Override
	public EnemyState getState() {
		return null;
	}

	@Override
	public void setState(EnemyState getState) {

	}
}