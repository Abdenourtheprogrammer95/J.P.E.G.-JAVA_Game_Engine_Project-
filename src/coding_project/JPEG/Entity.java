package coding_project.JPEG;

import java.util.Map;
import java.util.HashMap;

public abstract class Entity implements Movable, Droppable {
	private int hp, damage;
	private String name;
    protected Map<Item, DropRule> dropTable;
    private double x_speed = 1, y_speed = 1, x_pos = 0, y_pos = 0;

    public Entity() {
        initDropTable();
        this.hp = 1;
        this.damage = 1;
        this.name = null;
    }

	public Entity(int hp, int damage) {
		this.hp = hp;
		this.damage = damage;
	}

	public Entity(String name, String dropped) {
		this.hp = 1;
		this.damage = 1;
		this.name = name;
	}

	public Entity(int hp, int damage, String name) {
		this.hp = hp;
		this.damage = damage;
		this.name = name;
	}

    public Entity(int hp, int damage, String name, String dropped) {
        this.hp = hp;
        this.damage = damage;
        this.name = name;
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

	public void displayEntity() {
		System.out.println("Entity "+name+" has "+hp+" health points and can deal "+damage+" damage points.");
	}

    /* implement the drop table!!!
    @Override
    public void canDrop() {
        System.out.println("Entity "+getName()+" can drop "+getDropped()+".");
    } */

    protected abstract void initDropTable();

    @Override
    public Map<Item, Integer> generateDrops() {
        Map<Item, Integer> drops = new HashMap<>();

        for (var entry : dropTable.entrySet()) {
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

}