package coding_project.JPEG;

import java.awt.image.BufferedImage;
import java.util.Random;

public enum DroppableItems implements Item {
    XP("sprites/experience_orb.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            p.setExperience(p.getExperience() + amount);
        }
    },

    // instead of teleporting you (like they do in Minecraft), ender pearls make you move faster
    ENDER_PEARL("sprites/ender_pearl.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            double xS = p.getXspeed(), yS = p.getXspeed(), am = 0.1*amount;

            if (xS+am<=5.0 && yS+am<=5.0) {
                p.setXspeed(xS + am);
                p.setYspeed(yS + am);
            }

            else {
                p.setXspeed(5.0);
                p.setYspeed(5.0);
            }
        }
    },

    GUN_POWDER("sprites/gunpowder.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            // ... ;
        }
    },

    // Some more thinking is needed below
    BONE("sprites/bone.png"), STRING("sprites/string.png") {
        @Override
        public void onAcquire(Player p, int amount) {

            // best-case scenario: the player one-shots the most resistant mob ("Enderman")
            int bD = p.getBowDamage();

            if (bD+amount <= 20) {
                p.setBowDamage(bD + amount);
            }

            else {
                p.setBowDamage(20);
            }
        }
    },

    // players can pick up ammunition from slayed skeletons (in this case: arrows)
    ARROW("sprites/arrow.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            p.setQuiver(1);
        }
    },

    // rotten flesh either grants you health or a poison effect, hence the negative value
    ROTTEN_FLESH("sprites/rotten_flesh.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            int randomOfTwoInts = new Random().nextBoolean() ? (int) -1.8 : 4;
            p.setHp(p.getHp() + randomOfTwoInts);
        }
    },

    // potatoes and carrots provide a health boost to the player when picked up
    POTATO("sprites/potato.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            p.setHp(p.getHp() + 1);
        }
    },

    CARROT("sprites/carrot.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            p.setHp(p.getHp() + 3);
        }
    },

    // iron ingots help mend (repair) the player's armor/weapon, or reinforce them
    IRON_INGOT("sprites/iron_ingot.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            if (p.getWeaponDamage() < 20) {
                p.setWeaponDamage(p.getWeaponDamage() + 1);
            } else if (p.getArmorResistance() + 5 < 200) {
                p.setArmorResistance(p.getArmorResistance() + 5);
            } else {
                p.setArmorResistance(200);
            }
        }
    },

    PHANTOM_MEMBRANE("sprites/phantom_membrane.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            // ...
        }
    },

    // similar to the way rotten flesh behaves
    SPIDER_EYE("sprites/spider_eye.png") {
        @Override
        public void onAcquire(Player p, int amount) {
            int randomOfTwoInts_2 = new Random().nextBoolean() ? (int) -2.2 : 2;
            p.setHp(p.getHp() + randomOfTwoInts_2);
        }
    };

    @Override
    public void onAcquire(Player p, int amount) {
        // default case: no effect...
    }

    private final BufferedImage sprite;

    DroppableItems(String spritePath) {
        this.sprite = SpriteLoader.load(spritePath);
    }

    @Override
    public BufferedImage getSprite() {
        return sprite;
    }
}