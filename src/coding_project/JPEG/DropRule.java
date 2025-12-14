package coding_project.JPEG;

import static java.lang.Math.random;

public class DropRule {
    private final double chance;
    private final int min;
    private final int max;

    public DropRule(double chance, int min, int max) {
        this.chance = chance;
        this.min = min;
        this.max = max;
    }

    public int rollAmount() {
        // No drop
        if (random() > chance) {
            return 0;
        }

        // Random between min and max (inclusive)
        return min + (int) (random() * (max - min + 1));
    }

}