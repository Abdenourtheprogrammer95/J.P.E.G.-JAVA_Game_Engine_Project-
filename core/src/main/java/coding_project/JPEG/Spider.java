package coding_project.JPEG;

import java.util.Map;

public class Spider extends Entity implements Monster {
    private final String path = "Image_s/Sprite_s/Spider/spider__idle.png";

    public Spider() {
		super(8, 2, "Spider", 6.0f);
        this.threshold = 16f;
		setSprite(SpriteLoader.load(path));
	}

	public Spider(int hp, int damage, float moveSpeed) {
		super(hp, damage, "Spider", moveSpeed);
        this.threshold = 16f;
		setSprite(SpriteLoader.load(path));
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
    public EnemyType getEnemyType() {
        return EnemyType.SPIDER;
    }

    @Override
    public String getAnimationPrefix(EnemyState state) {
        switch (state) {
            case IDLE:
                return "spider__idle";
            case WALK:
            case CHASE:
                return "spider__walk";
            case ATTACK:
            case HURT:
            case  DEAD:
                return null;
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
