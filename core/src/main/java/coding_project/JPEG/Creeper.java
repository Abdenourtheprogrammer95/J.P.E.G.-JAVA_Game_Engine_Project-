package coding_project.JPEG;

import java.util.Map;

public class Creeper extends Entity implements Monster {
    private String path = "Image_s/Sprite_s/Creeper/creeper__idle.png";

    public Creeper() {
		super(10, 43, "Creeper");
        this.threshold = 16f;
        setSprite(SpriteLoader.load(path));
	}

	public Creeper(int hp, int damage) {
		super(hp, damage, "Creeper");
        this.threshold = 16f;
		setSprite(SpriteLoader.load(path));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.GUN_POWDER, new DropRule(0.6667, 0, 5),
                DroppableItems.XP, new DropRule(0.9, 1, 5)
                );
    }

    @Override
    public EnemyType getEnemyType() {
        return EnemyType.CREEPER;
    }

    @Override
    public String getAnimationPrefix(EnemyState state) {
        switch (state) {
            case IDLE:
                return "creeper__idle";
            case WALK:
            case CHASE:
                return "creeper__walk";
            case ATTACK:
                return "creeper__explode";
            case HURT:
            case  DEAD:
                return "creeper__hurt";
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
