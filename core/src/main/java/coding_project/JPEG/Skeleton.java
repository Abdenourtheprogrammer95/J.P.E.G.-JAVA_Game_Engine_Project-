package coding_project.JPEG;

import java.util.Map;

public class Skeleton extends Entity implements Monster {
    private String path = "Image_s/Sprite_s/Skeleton/skeleton__idle.png";

    public Skeleton() {
		super(10, 2, "Skeleton");
        this.threshold = 16f;
		setSprite(SpriteLoader.load(path));
	}

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.ARROW, new DropRule(0.5, 0, 5),
                DroppableItems.BONE, new DropRule(0.6, 0, 5),
                DroppableItems.XP, new DropRule(0.9, 1,5)
                );
    }

    public Skeleton(int hp, int damage, float moveSpeed) {
        super(hp, damage, "Skeleton",  moveSpeed);
        this.threshold = 16f;
        setSprite(SpriteLoader.load(path));
    }

    @Override
    public EnemyType getEnemyType() {
        return EnemyType.SKELETON;
    }

    @Override
    public String getAnimationPrefix(EnemyState state) {
        switch (state) {
            case IDLE:
                return "skeleton__idle";
            case WALK:
            case CHASE:
                return "skeleton__walk";
            case ATTACK:
                return "skeleton__bow__attack";
            case HURT:
            case  DEAD:
                return null;
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
