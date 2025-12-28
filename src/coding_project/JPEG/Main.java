package coding_project.JPEG;

import java.util.*;

class Main {
    public static void main(String[] args) {

        //1st Enderman test:
        Entity ender_man = new Enderman();
        Map<Item, Integer> drops = ender_man.generateDrops();
        System.out.println(ender_man.getClass());
        System.out.println("Enderman: " + drops);

        for (var entry : drops.entrySet()) {
            boolean bool_1 = entry.getKey()==DroppableItems.ENDER_PEARL && (entry.getValue()<=4 && entry.getValue()>=0);
            boolean bool_2 = entry.getKey()==DroppableItems.XP && (entry.getValue()<=5 && entry.getValue()>=1);
            if (bool_1 || bool_2) {
                System.out.println("Range for "+entry.getKey()+" is well-valued: Test passed.");
            };
        }

        //2nd Enderman test:
        int trials = 1000;
        int pearlCount = 0;
        int XPCount = 0;
        for (int i = 0; i < trials; i++) {
            Map<Item, Integer> randomnessDrops = ender_man.generateDrops();
            if (randomnessDrops.containsKey(DroppableItems.ENDER_PEARL)) {
                pearlCount += randomnessDrops.get(DroppableItems.ENDER_PEARL);
            }

            if (randomnessDrops.containsKey(DroppableItems.XP)) {
                XPCount += randomnessDrops.get(DroppableItems.XP);
            }
        }

        System.out.println("Average ender_pearl(s) per kill: "+(double) pearlCount/trials);
        System.out.println("Average XP per kill: "+(double) XPCount/trials);

        /*

        List<Entity> ent = new ArrayList<>();

        ent.add(new Enderman());
        ent.add(new Creeper());
        ent.add(new Skeleton());
        ent.add(new Zombie());
        ent.add(new Phantom());
        ent.add(new Spider());

        Entity player = new Player("Steve");
        ent.add(player);

        for (Entity e : ent) {
            e.displayEntity();
            e.canDrop();
            System.out.println(); // skipping line to separate entities' info
        }

        // type-casting "player" object of type Entity to Player type, in order to use its specific methods
        System.out.println("Player "+((Player) player).getName()+" currently has "+((Player) player).getQuiver()+" arrows.");


        List<DroppedItem> droppedItems = new ArrayList<>();

        for (DroppedItem item : droppedItems) {
            item.enemyBehaviour(player);
        }

        droppedItems.removeIf(DroppedItem::isCollected);

        for (DroppedItem item : droppedItems) {
            item.render(graphics);
        }

        */

    }
}