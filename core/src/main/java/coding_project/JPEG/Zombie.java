package coding_project.JPEG;

import java.util.Map;

public class Zombie extends Entity implements Monster {
    private String path = "Image_s/Sprite_s/Zombie/zombie__idle.png";

	public Zombie() {
		super(10, 3, "Zombie", 0.92f, 14f, 16f);
        this.threshold = 35f;
        setSprite(SpriteLoader.load(path));
	}

	public Zombie(int hp, int damage, float moveSpeed) {
		super(hp, damage, "Zombie",  moveSpeed);
        this.threshold = 35f;
		setSprite(SpriteLoader.load(path));
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
    public EnemyType getEnemyType() {
        return EnemyType.ZOMBIE;
    }

    @Override
    public String getAnimationPrefix(EnemyState state) {
        switch (state) {
            case IDLE:
                return "zombie__idle";
            case WALK:
            case CHASE:
                return "zombie__walk";
            case ATTACK:
                return "zombie__punch__attack";
            case HURT:
            case  DEAD:
                return "zombie__hurt";
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
