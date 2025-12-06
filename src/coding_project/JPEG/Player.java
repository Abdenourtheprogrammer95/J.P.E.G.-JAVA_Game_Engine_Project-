package coding_project.JPEG;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class Player extends Entity {
    private static int bow_damage;
    private static int weapon_damage;
	private static int armor_resistance;
    private static int quiver = 0;
    private static int experience = 0;

	public Player() {
		super(600, 1);
		this.weapon_damage = 1;
		this.armor_resistance = 10;
	}

	public Player(String name) {
		super(600, 1, name);
		this.weapon_damage = weapon_damage;
		this.armor_resistance = armor_resistance;
	}

	public Player(int armor_resistance, int weapon_damage) {
		super(600, 1);
		this.weapon_damage = weapon_damage;
		this.armor_resistance = armor_resistance;
	}

	public Player(int armor_resistance, int weapon_damage, String name) {
		super(600, 1, name);
		this.weapon_damage = weapon_damage;
		this.armor_resistance = armor_resistance;
	}

    public Player(int armor_resistance, int weapon_damage, String name, int experience) {
        super(600, 1, name);
        this.weapon_damage = weapon_damage;
        this.armor_resistance = armor_resistance;
        this.experience = experience;
    }

    public static int getBow_damage() {
        return bow_damage;
    }

    public static void setBow_damage(int bow_damage) {
        Player.bow_damage = bow_damage;
    }

    public int getWeapon_damage() {
		return weapon_damage;
	}

	public void setWeapon_damage(int weapon_damage) {
		this.weapon_damage = weapon_damage;
	}

	public int getArmor_resistance() {
		return armor_resistance;
	}

	public void setArmor_resistance(int armor_resistance) {
		this.armor_resistance = armor_resistance;
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

	public void gearImprove(String dropped) {
		setHp(getHp() + getArmor_resistance());
		System.out.println("Your gear has been improved and you now have "+getHp()+" health points.");

        /* There still are some things to fix below !!! */
		String result;
		switch (dropped) {
            case "xp":
                setExperience(getExperience()+1);
				break;
			case "enderpearl": // instead of teleporting you, enderpearls make you move quicker
				super.setXspeed(getXspeed()+0.1);
                super.setYspeed(getYspeed()+0.1);
				break;
			case "gunpowder":
				result = "gunpowder";
				break;
			case "bone", "string": // Some more thinking is needed here
				setBow_damage(getBow_damage()+1);
				break;
			case "arrow":
				setQuiver(1);
				break;
			case "rotten flesh":
                /* rotten flesh either grants you health or a poison effect hence the negative value */
                int randomOfTwoInts = new Random().nextBoolean() ? (int) -1.8 : 4;
                super.setHp(getHp()+randomOfTwoInts);
				break;
			case "potato":
				super.setHp(getHp()+1);
				break;
			case "carrot":
                super.setHp(getHp()+3);
				break;
			case "iron ingot":
                if (getWeapon_damage()<20) {
				    setWeapon_damage(getWeapon_damage()+1);
                }
                else if (getArmor_resistance()+5<200) {
                    setArmor_resistance(getArmor_resistance()+5);
                }
                else {
                    setArmor_resistance(200);
                }
				break;
			case "phantom membrane":
				result = "phantom membrane";
				break;
            case "spider eye":
                int randomOfTwoInts_2 = new Random().nextBoolean() ? (int) -2.2 : 2;
                super.setHp(getHp()+randomOfTwoInts_2);
				break;
		}

		System.out.println("Thanks to the dropped item "+dropped+" you collected, BLA BLA was improved.");
	}

    /* @Override
    public void canDrop() {
        System.out.println("Player "+getName()+" just dropped all of its xp!");
    } */

    @Override
    protected void initDropTable() {
        dropTable = Map.of(
                DroppableItems.XP, new DropRule(0.9, 1, 3)
        );
    }
}