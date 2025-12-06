package coding_project.JPEG;

import java.util.*;

class Main {
    public static void main(String[] args) {

        List<Entity> ent = new ArrayList<>();

        ent.add(new Enderman());
        ent.add(new Creeper());
        ent.add(new Skeleton());
        ent.add(new Zombie());
        ent.add(new Phantom());
        ent.add(new Spider());

        Entity player = new Player("Steve");
        ent.add(player);
/*
        for (Entity e : ent) {
            e.displayEntity();
            e.canDrop();
            System.out.println(); // skipping line to separate entities' info
        }
*/

        // type-casting "player" object of type Entity to Player type, in order to use its specific methods
        System.out.println("Player "+((Player) player).getName()+" currently has "+((Player) player).getQuiver()+" arrows.");

    }
}