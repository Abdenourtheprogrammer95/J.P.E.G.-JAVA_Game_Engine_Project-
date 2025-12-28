package coding_project.JPEG;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public abstract class Entity implements Movable, Droppable {
	private int hp, damage;
	private String name;
    protected Map<Item, DropRule> dropTable;
	protected BufferedImage sprite;
	private double x_speed = 1, y_speed = 1, x_pos = 0, y_pos = 0;
	int threshold; //sets the spot distance for hostile entities

	/* All constructors must ensure that the drops table is initialized, whenever any entity's created.
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

	public Entity(int hp, int damage, String name, double x_speed, double y_speed) {
		initDropTable();
		this.hp = hp;
		this.damage = damage;
		this.name = name;
		this.x_speed = x_speed;
		this.y_speed = y_speed;
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

	public double getXspeed() {
		return this.x_speed;
	}

	public void setXspeed(double x_speed) {
		this.x_speed = x_speed;
	}

	public double getYspeed() {
		return this.y_speed;
	}

	public void setYspeed(double y_speed) {
		this.y_speed = y_speed;
	}

	public double getXpos() {
		return this.x_pos;
	}

	public void setXpos(double x_pos) {
		this.x_pos = x_pos;
	}

	public double getYpos() {
		return this.y_pos;
	}

	public void setYpos(double y_pos) {
		this.y_pos = y_pos;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public void displayEntity() {
		System.out.println("Entity "+name+" has "+hp+" health points and can deal "+damage+" damage points.");
	}

    protected abstract void initDropTable();

	public void takeDamage(int damage) {
		setHp(getHp() - damage);
	}

	public void triggerChase(Entity enemy, Player player) {
		double dx = player.getXpos()-enemy.getXpos(),
				dy = player.getYpos()-enemy.getYpos(),
				distanceToPlayer = Math.sqrt(dx*dx+dy*dy);
		// monster already at player location
		if (distanceToPlayer == 0) return;

		double stepX = (dx/distanceToPlayer) * enemy.getXspeed();
		double stepY = (dy/distanceToPlayer) * enemy.getYspeed();

		move(enemy, stepX, stepY, enemy.getXspeed(), enemy.getYspeed());
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
		x_pos += x_speed;
        y_pos += y_speed;
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