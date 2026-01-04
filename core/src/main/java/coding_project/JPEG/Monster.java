package coding_project.JPEG;

import com.mygdx.game.PlayerActor;

public interface Monster {
    public abstract String getAnimationPrefix(EnemyState state);

    EnemyType getEnemyType();

    default void updateAI(PlayerActor player, Entity enemy) {
        float dx = player.getX() - enemy.getXpos(), dy = player.getY() - enemy.getYpos(), dist2 = dx * dx + dy * dy;
        float threshold = enemy.threshold;

        if (dist2 < threshold * threshold) {
            double dist = Math.sqrt(dist2);
            if (dist > 0.0001f) {
                // Compute normalized direction
                float dirX = (float) (dx / dist), dirY = (float) (dy / dist);

                // Apply velocity using constant moveSpeed
                float vx = dirX * enemy.getMoveSpeed(), vy = dirY * enemy.getMoveSpeed();

                // Move the enemy
                enemy.move(enemy, vx, vy, 1f); // scale factor = 1f, moveSpeed already applied
            }
            enemy.setCurrentState(EnemyState.CHASE);
        } else {
            enemy.setCurrentState(EnemyState.IDLE);
        }

        System.out.println(
            enemy.getClass().getSimpleName() +
                " state=" + enemy.getCurrentState() +
                " speed=(" + enemy.getMoveSpeed() + ")"
        );
    }
}
