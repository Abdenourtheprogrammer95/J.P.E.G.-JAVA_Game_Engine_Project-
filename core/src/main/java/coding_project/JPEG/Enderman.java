package coding_project.JPEG;

import java.util.Map;

public class Enderman extends Entity implements Monster {
	private EnemyState state;
    private String path = "Image_s/Sprite_s/Enderman/enderman__idle.png";

	public Enderman() {
		super(20, 7, "Enderman", 1.2f);
        this.threshold = 64f;
		setSprite(SpriteLoader.load(path));
	}

	public Enderman(int hp, int damage, float moveSpeed) {
		super(hp, damage, "Enderman", moveSpeed);
        this.threshold = 64f;
		setSprite(SpriteLoader.load(path));
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
        // teleportation (later)
	}

    @Override
    public EnemyType getEnemyType() {
        return EnemyType.ENDERMAN;
    }

    @Override
    public String getAnimationPrefix(EnemyState state) {
        switch (state) {
            case IDLE:
                return "enderman__idle";
            case WALK:
                return "enderman__calm__walk";
            case CHASE:
                return "enderman__chase__walk";
            case ATTACK:
                return null;
            case HURT:
            case  DEAD:
                return "enderman__hurt";
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
