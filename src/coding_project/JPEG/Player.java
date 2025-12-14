package coding_project.JPEG;

import java.util.Map;

public class Player extends Entity {
    private static int bowDamage;
    private static int weaponDamage;
	private static int armorResistance;
    private static int quiver = 0;
    private static int experience = 0;

	public Player() {
		super(600, 1);
		this.weaponDamage = 1;
		this.armorResistance = 10;
	}

	public Player(String name) {
		super(600, 1, name);
	}

	public Player(int weaponDamage, int armor_resistance) {
		super(600, 1);
		this.weaponDamage = weaponDamage;
		this.armorResistance = armor_resistance;
	}

	public Player(String name, int weaponDamage, int armorResistance) {
		super(600, 1, name);
		this.weaponDamage = weaponDamage;
		this.armorResistance = armorResistance;
	}

    public Player(String name, int weaponDamage, int armorResistance, int experience) {
        super(600, 1, name);
        this.weaponDamage = weaponDamage;
        this.armorResistance = armorResistance;
        this.experience = experience;
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

    public int getQuiver() {
        return quiver;
    }

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
}