/*
package coding_project.JPEG;

import java.util.Map;

public class Phantom extends Entity implements Monster {
    private String path = "Image_s/Sprite_s/Enemy.png";

    public Phantom() {
		super(10, 2, "Phantom", 1.8, 1.8);
		setSprite(SpriteLoader.load(path));
	}

	public Phantom(int hp, int damage, double x_speed, double y_speed) {
		super(hp, damage, "Phantom", x_speed, y_speed);
		setSprite(SpriteLoader.load(path));
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
    public EnemyType getEnemyType() {
        return EnemyType.PHANTOM;
    }

    @Override
    public String getAnimationPrefix(EnemyState state) {
        switch (state) {
            case IDLE:
                return "phantom__idle";
            case WALK:
            case CHASE:
                return "phantom__walk";
            case ATTACK:
                return "phantom__explode";
            case HURT:
            case  DEAD:
                return "phantom__hurt";
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
*/
