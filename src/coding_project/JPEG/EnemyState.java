package coding_project.JPEG;

enum EnemyState {
    IDLE() {
        @Override
        void behave(Entity enemy, Player player) {
            enemy.triggerPathFinding(); //I'll work on this method in a bit...
        }
    },

    CHASING() {
        @Override
        void behave(Entity enemy, Player player) {
            // different thresholds per entity (some notice you at greater distances)
            double threshold = 0;
            double dx = player.getXpos()-enemy.getXpos(), dy = player.getYpos()-enemy.getYpos();
            if (dx<threshold || dy<threshold) {
                enemy.triggerChase(enemy, player);
            }
            else {
                enemy.setState(IDLE);
            }
        }
    },

    ATTACKING() {
        @Override
        void behave(Entity enemy, Player player) {
            player.takeDamage(enemy.getDamage());
        }
    },

    DEAD() {
        @Override
        void behave(Entity enemy, Player player) {
            enemy.markForRemoval();
        }
    };

    abstract void behave(Entity enemy, Player player);
}