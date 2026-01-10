package coding_project.JPEG;

import com.mygdx.game.GameScreenDebug;

import java.util.Map;

public class Creeper extends Entity implements Monster, DamageSource {
    private final String path = "Image_s/Sprite_s/Creeper/creeper__idle.png";

    public Creeper() {
		super(10, 43, "Creeper", 5.0f, 10f/16f, 15f/16f);
        this.threshold = 16f;
        setSprite(SpriteLoader.load(path));
	}

	public Creeper(int hp, int damage) {
		super(hp, damage, "Creeper", 5.0f, 10f/16f, 15f/16f);
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
    protected void onAttackRelease() {
        if (attackTarget instanceof AttackTarget) {
            AttackTarget target = (AttackTarget) attackTarget;
            if (!target.isDead()) {
                target.takeDamage(getDamage());
            }
        }

        attacking = false;
        attackTriggered  = false;
    }

    @Override
    public void onAttackFinished() {
        // v User-Added DEBUGGING v
        System.out.println("[CREEPER] Attack finished → DEAD");
        System.out.println(
            "[CREEPER] frame=" + GameScreenDebug.frame +
                " exploded at x=" + getXpos() +
                " y=" + getYpos()
        );
        // ^ User-Added DEBUGGING ^

        selfKill();
    }

    @Override
    public boolean canStartAttack(LivingEntity target) {
        float dx = target.getCenterX() - getCenterX(), dy = target.getCenterY() - getCenterY(),
            dist2 = dx * dx + dy * dy, explodeRange = threshold * 0.08f; // This tuning to ensure that creepers
                                                                         // explode from a set distance
        return dist2 <= explodeRange * explodeRange;
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
            case DEAD:
                return "creeper__explode";
            case HURT:
                return "creeper__hurt";
            default:
                throw new IllegalArgumentException("Unknown state " + state);
        }
    }
}
