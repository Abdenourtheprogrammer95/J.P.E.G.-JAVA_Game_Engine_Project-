package coding_project.JPEG;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static coding_project.JPEG.EnemyState.IDLE;

public abstract class Entity implements Movable, Droppable {
	private int hp, damage;
	private String name;

    protected Map<Item, DropRule> dropTable;
	protected BufferedImage sprite;

	private float moveSpeed, x_pos = 0, y_pos = 0;

	float threshold; //sets the spot distance for hostile entities
    private boolean markedForRemoval = false;
    protected float collisionWidth;
    protected float collisionHeight;

    private EnemyState currentState = IDLE;

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
		this.hp = hp;
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
        this.currentState = state;
    }

    void markForRemoval() {
        this.markedForRemoval = true;
    }

    public boolean isMarkedForRemoval() {
        return this.markedForRemoval;
    }

    public void displayEntity() {
		System.out.println("Entity "+name+" has "+hp+" health points and can deal "+damage+" damage points.");
	}

    protected abstract void initDropTable();

	public void takeDamage(int damage) {
		setHp(getHp() - damage);
	}

	/* The following methods are only relevant to hostile entities (monsters),
	   but I decided to not have them as default methods in the interface: */

	public void triggerPathFinding(Entity enemy, Player player) {
		double dx = player.getXpos() - enemy.getXpos();
		double dy = player.getYpos() - enemy.getYpos();

		double stepX = Math.signum(dx);
		double stepY = Math.signum(dy);

		if (Math.abs(dx) > Math.abs(dy)) {
			move(enemy, (float) stepX, 0, getMoveSpeed());
		} else {
			move(enemy, 0, (float) stepY, getMoveSpeed());
		}
	}

	public void triggerChase(Entity enemy, Player player) {
		double dx = player.getXpos()-enemy.getXpos(),
				dy = player.getYpos()-enemy.getYpos(),
				distanceToPlayer = Math.sqrt(dx*dx+dy*dy);
		// monster already at player location
		if (distanceToPlayer == 0) return;

		float stepX = (float) (dx/distanceToPlayer), stepY = (float) (dy/distanceToPlayer);

		move(enemy, stepX, stepY, enemy.getMoveSpeed());
	}

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
	public void canMove() {
		x_pos += moveSpeed;
        y_pos += moveSpeed;
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
