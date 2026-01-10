package coding_project.JPEG;

import java.util.Map;

public class Player extends Entity implements LivingEntity, AttackTarget {
    private static int weaponDamage, armorResistance, experience = 0;
    private static final float HITBOX_WIDTH = 14f/16f, HITBOX_HEIGHT = 16f/16f;

    @Deprecated
    private static int bowDamage, quiver = 0;

    private void initHitBoxes() {
        this.collisionHeight = HITBOX_HEIGHT;
        this.collisionWidth = HITBOX_WIDTH;
    }

	public Player() {
		super(600, 1, "Steve");
		this.weaponDamage = 1;
		this.armorResistance = 10;
        initHitBoxes();
	}

	public Player(String name) {
		super(600, 1, name);
        initHitBoxes();
	}

	public Player(int weaponDamage, int armor_resistance) {
		super(600, 1, "Steve");
		this.weaponDamage = weaponDamage;
		this.armorResistance = armor_resistance;
        initHitBoxes();
	}

	public Player(String name, int weaponDamage, int armorResistance) {
		super(600, 1, name);
		this.weaponDamage = weaponDamage;
		this.armorResistance = armorResistance;
        initHitBoxes();
	}

    public Player(String name, int weaponDamage, int armorResistance, int experience) {
        super(600, 1, name);
        this.weaponDamage = weaponDamage;
        this.armorResistance = armorResistance;
        this.experience = experience;
        initHitBoxes();
    }

    public static int getBowDamage() {
        return bowDamage;
    }

    public void setBowDamage(int bowDamage) {
        this.bowDamage = bowDamage;
    }

    public int getWeaponDamage() {
		return weaponDamage;
	}

	public void setWeaponDamage(int weaponDamage) {
		this.weaponDamage = weaponDamage;
	}

	public int getArmorResistance() {
		return armorResistance;
	}

	public void setArmorResistance(int armorResistance) {
		this.armorResistance = armorResistance;
	}

    @Deprecated
    public int getQuiver() {
        return quiver;
    }

    @Deprecated
    public void setQuiver(int arrowsNb) {
        this.quiver += arrowsNb;
    }

    public static int getExperience() {
        return experience;
    }

    public static void setExperience(int experience) {
        Player.experience = experience;
    }

    public void gearImprove() {
        setHp(getHp() + getArmorResistance());
        System.out.println("Your gear has been improved and you now have " + getHp() + " health points.");

        Map<Item, Integer> drops = super.generateDrops();
        drops.forEach((item, amount) -> item.onAcquire(this, amount));
    }

    @Override
    protected void initDropTable() {
        double chance;
        if (getExperience()>7) {
            chance = 1.0;
        }
        else {
            chance = 0.5;
        }
        dropTable = Map.of(
                DroppableItems.XP, new DropRule(chance, 0, 7)
        );
    }

    @Override
    public void takeDamage(int damage) {
        setHp(getHp() - damage);
        System.out.println("[PLAYER] took " + damage + "damage -> hp = " + getHp());
    }

    @Override
    public boolean isDead() {
        return getHp()<=0;
    }
}
