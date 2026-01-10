package coding_project.JPEG;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static coding_project.JPEG.EnemyState.IDLE;

public abstract class Entity implements Movable, Droppable, DamageSource {
	private int hp, damage;
	private String name;

    protected Map<Item, DropRule> dropTable;
	protected BufferedImage sprite;

	private float moveSpeed, x_pos = 0, y_pos = 0, moveDX = 0f, moveDY = 0f;

	float threshold; //sets the spot distance for hostile entities
    private boolean markedForRemoval = false;
    protected float collisionWidth;
    protected float collisionHeight;

    private EnemyState currentState = IDLE;

    protected boolean attacking = false;
    protected float attackTimer = 0f;
    protected float attackDuration = 0.25f; // seconds, tweak freely
    protected boolean attackTriggered = false;
    protected boolean hurt = false;
    protected float hurtTimer = 0f;
    protected float hurtDuration = 0.2f; // tweak freely
    protected LivingEntity attackTarget;

	/* All constructors must ensure that the drops table is initialized whenever any entity is created.
	Otherwise, a NullPointerException will be triggered when trying to access it. */

    public Entity() {
        initDropTable();
        this.hp = 1;
        this.damage = 1;
        this.name = null;
    }

    public Entity(String name) {
		initDropTable();
        this.hp = 1;
        this.damage = 1;
        this.name = name;
    }

	public Entity(int hp, int damage) {
		initDropTable();
		this.hp = hp;
		this.damage = damage;
	}

	public Entity(int hp, int damage, String name) {
		initDropTable();
		this.hp = hp;
		this.damage = damage;
		this.name = name;
	}

	public Entity(int hp, int damage, String name, float moveSpeed) {
		initDropTable();
		this.hp = hp;
		this.damage = damage;
		this.name = name;
		this.moveSpeed = moveSpeed;
	}

    public Entity(int hp, int damage, String name, float moveSpeed, float collisionWidth, float collisionHeight) {
        initDropTable();
        this.hp = hp;
        this.damage = damage;
        this.name = name;
        this.moveSpeed = moveSpeed;
        this.collisionWidth = collisionWidth;
        this.collisionHeight = collisionHeight;
    }

	public int getHp() {
		return this.hp;
	}

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    public int getDamage() {
		return this.damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getMoveSpeed() {
		return this.moveSpeed;
	}

	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public float getXpos() {
		return this.x_pos;
	}

	public void setXpos(float x_pos) {
		this.x_pos = x_pos;
	}

	public float getYpos() {
		return this.y_pos;
	}

	public void setYpos(float y_pos) {
		this.y_pos = y_pos;
	}

    public boolean intersects(Entity other) {
        return getXpos() < other.getXpos() + other.getCollisionWidth() &&
            getXpos() + getCollisionWidth() > other.getXpos() &&
            getYpos() < other.getYpos() + other.getCollisionHeight() &&
            getYpos() + getCollisionHeight() > other.getYpos();
    }

    public float getCenterX() {
        return getXpos() + getCollisionWidth() * 0.5f;
    }

    public float getCenterY() {
        return getYpos() + getCollisionHeight() * 0.5f;
    }

    public float getMoveDX() {
        return this.moveDX;
    }

    public float getMoveDY() {
        return this.moveDY;
    }

    public float getAttackTimer() {
        return attackTimer;
    }

    protected boolean isVerticallyAligned(LivingEntity target, float tolerance) {
        float dy = Math.abs(target.getCenterY() - getCenterY());
        return dy <= tolerance;
    }

    public void requestMove(float dx, float dy) {
        // ^ USER-ADDED DEBUGGING ^
        if (Float.isNaN(dx) || Float.isNaN(dy)) {
            System.err.println("[MOVE][ERROR] NaN request ignored");
            return;
        }

        System.out.println(
            "[MOVE][REQ] " + getName() +
                " dx=" + dx + " dy=" + dy
        );
        // ^ USER-ADDED DEBUGGING ^

        this.moveDX = dx;
        this.moveDY = dy;
    }

    public void clearMoveRequest() {
        this.moveDX = 0f;
        this.moveDY = 0f;
    }

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

    public float getCollisionWidth()  { return collisionWidth; }

    public float getCollisionHeight() { return collisionHeight; }

    public void setCollisionWidth(float collisionWidth) { this.collisionWidth= collisionWidth; }

    public void setCollisionHeight(float collisionHeight) {this.collisionHeight= collisionHeight; }

    public EnemyState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(EnemyState state) {
        // v User-Added DEBUGGING v
        if (this.currentState != state) {
            System.out.println(
                "[STATE] " + getName() + ": " +
                    this.currentState + " -> " + state
            );
        }
        // ^ User-Added DEBUGGING ^

        if (currentState == EnemyState.DEAD) return; // Hard lock
        this.currentState = state;
        if (state == EnemyState.DEAD) {
            markForRemoval();
        }
    }

    public void markForRemoval() {
        this.markedForRemoval = true;
    }

    public boolean isMarkedForRemoval() {
        return this.markedForRemoval;
    }

    public void selfKill() {
        hp = 0;
        onDeath();
    }

    protected void onDeath() {
        setCurrentState(EnemyState.DEAD);
    }

    public void update(float delta) {
        if (this instanceof Skeleton) {
            System.out.println(
                "[SKEL DEBUG] state=" + currentState +
                    " attacking=" + attacking +
                    " canMove=" + canMove() +
                    " DX=" + getMoveDX() +
                    " DY=" + getMoveDY() +
                    " x=" + getXpos() +
                    " y=" + getYpos()
            );
        }

        updateAttack(delta);
        updateHurt(delta);
    }

    public void startAttack(LivingEntity target) {
        // v USER-ADDED DEBUGGING v
        System.out.println("[ATTACK] startAttack()");
        // ^ USER-ADDED DEBUGGING ^

        if (attackTriggered) return;

        attackTriggered = true;
        attacking = true;
        attackTimer = 0f;
        attackTarget = target;

        setCurrentState(EnemyState.ATTACK);
        clearMoveRequest(); // HARD STOP: I don't want entities to keep moving while they attack
    }

    public void updateAttack(float delta) {
        // v USER-ADDED DEBUGGING v
        System.out.println(
            "[ATTACK] " + name +
                " state=" + currentState +
                " attacking=" + attacking +
                " timer=" + attackTimer +
                " duration=" + attackDuration
        );
        // ^ USER-ADDED DEBUGGING ^

        if (!attacking || currentState!=EnemyState.ATTACK) return;

        attackTimer += delta;

        clearMoveRequest(); // ensure no movement during attack

        if (attackTimer >= attackDuration) {
            attackTimer = 0f;
            onAttackRelease();

            if (!attacking) {
                onAttackFinished(); // Finalizing attack
            }
        }

        // v USER-ADDED DEBUGGING v
        System.out.println(
            "[ATTACK] timer=" + attackTimer +
                " attacking=" + attacking +
                " state=" + currentState
        );
        // ^ USER-ADDED DEBUGGING ^
    }

    public void onAttackFinished() {
        attacking = false;
        attackTriggered = false;
        setCurrentState(EnemyState.CHASE);
    }

    protected void onAttackRelease() {
        attacking = false;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean canStartAttack(LivingEntity target) {
        if (!(target instanceof Entity)) return false;
        Entity t = (Entity) target;
        return intersects(t); // default attack: melee
    }

    @Deprecated
    public void displayEntity() {
		System.out.println("Entity "+name+" has "+hp+" health points and can deal "+damage+" damage points.");
	}

    protected abstract void initDropTable();

	public void takeDamage(int damage) {
		setHp(getHp() - damage);

        if (getHp() > 0) {
            startHurt();
        } else {
            setCurrentState(EnemyState.DEAD);
        }
	}

    public void startHurt() {
        /* Hard cancelling any attack to follow the self-imposed rule:
        an entity cannot  attack and hurt at the same time */
        attacking = false;
        attackTriggered = false;
        attackTimer = 0;

        hurt = true;
        hurtTimer = 0f;
        setCurrentState(EnemyState.HURT);
    }

    public void updateHurt(float delta) {
        if (!hurt) return;

        hurtTimer += delta;
        clearMoveRequest();

        if (hurtTimer >= hurtDuration) {
            hurt = false;
            hurtTimer = 0f;
            setCurrentState(EnemyState.IDLE); // or CHASE later via AI
        }
    }

	/* The following methods are only relevant to hostile entities (monsters),
	   but I decided to not have them as default methods in the interface: */

	public void triggerPathFinding(LivingEntity target) {
		double dx = target.getXpos() - getXpos(), dy = target.getYpos() - getYpos(),
            stepX = Math.signum(dx), stepY = Math.signum(dy);

        if (Math.abs(dx) > Math.abs(dy)) {
            requestMove((float) stepX, 0);
        } else {
            requestMove(0, (float) stepY);
        }
    }

    public void triggerChase(LivingEntity target) {
        float dx = target.getCenterX() - getCenterX(), dy = target.getCenterY() - getCenterY(),
            len2 = dx * dx + dy * dy;

        if (len2 < 0.0001f) {
            clearMoveRequest();
            return;
        }

        float invLen = 1f / (float) Math.sqrt(len2);
        requestMove(dx * invLen, dy * invLen);
    }

    /*
	public void triggerChase(LivingEntity target) {
		double dx = target.getXpos()-getXpos(), dy = target.getYpos()-getYpos(),
				distanceToPlayer = Math.sqrt(dx*dx+dy*dy);

        // monster already at player location
		if (distanceToPlayer < 0.001f) {
            clearMoveRequest();
            return;
        };

		float stepX = (float) (dx/distanceToPlayer), stepY = (float) (dy/distanceToPlayer);
        requestMove(stepX, stepY);
	}
    */

    @Override
    public Map<Item, Integer> generateDrops() {
        Map<Item, Integer> drops = new HashMap<>();

        for (var entry : dropTable.entrySet()) { // using the "var" keyword, so I don't have to declare a type
            Item item = entry.getKey();
            DropRule rule = entry.getValue();

            int amount = rule.rollAmount();
            if (amount > 0) {
                drops.put(item, amount);
            }
        }

        return drops;
    }

	@Override
	public boolean canMove() {
        return !attacking;
	}

    public boolean isDead() {
        return currentState == EnemyState.DEAD;
    }

	public List<DroppedItem> drop(float x, float y) {
		List<DroppedItem> worldDrops = new ArrayList<>();

		Map<Item, Integer> drops = generateDrops();
		for (var entry : drops.entrySet()) {
			worldDrops.add(
					new DroppedItem(entry.getKey(), entry.getValue(), x, y)
			);
		}

		return worldDrops;
	}
}
