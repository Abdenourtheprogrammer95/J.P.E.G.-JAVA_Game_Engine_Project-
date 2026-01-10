package coding_project.JPEG;

public interface Monster {
    String getAnimationPrefix(EnemyState state);
    EnemyType getEnemyType();

    default boolean canAttack() {
        return true;
    }

    default void updateAI(LivingEntity target, Entity enemy) {
        // v USER-ADDED DEBUGGING v
        System.out.println(
            "[AI] " + enemy.getName() +
                " state=" + enemy.getCurrentState() +
                " dist=" + String.format("%.3f",
                Math.hypot(
                    target.getXpos() - enemy.getXpos(),
                    target.getYpos() - enemy.getYpos()
                )
            )
        );

        System.out.println(
            "[HITBOX] " + enemy.getName() +
                " pos=(" + enemy.getXpos() + "," + enemy.getYpos() + ")" +
                " size=(" + enemy.getCollisionWidth() + "," + enemy.getCollisionHeight() + ")" +
                " center=(" + enemy.getCenterX() + "," + enemy.getCenterY() + ")" +
                " | PLAYER center=(" +
                target.getCenterX() + "," + target.getCenterY() + ")"
        );
        // ^ USER-ADDED DEBUGGING ^

        EnemyState state = enemy.getCurrentState();

        if (enemy.isAttacking() || state == EnemyState.HURT || state == EnemyState.DEAD) {
            return;
        }

        float dx = target.getCenterX() - enemy.getCenterX(), dy = target.getCenterY() - enemy.getCenterY(),
            dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 0.2f) {
            enemy.clearMoveRequest();
            return;
        }

        if (enemy.canStartAttack(target)) {
            enemy.setCurrentState(EnemyState.ATTACK);
            return;
        }

        if (dist <= enemy.threshold) {
            enemy.setCurrentState(EnemyState.CHASE);
        } else if (dist <= enemy.threshold * 1.5f) {
            enemy.setCurrentState(EnemyState.WALK);
        } else {
            enemy.setCurrentState(EnemyState.IDLE);
        }
    }
}
