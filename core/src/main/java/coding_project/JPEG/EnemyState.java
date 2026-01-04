package coding_project.JPEG;

public enum EnemyState {

    IDLE {
        @Override
        public void behave(Entity enemy, Player player){
            enemy.setMoveSpeed(0);
        }
    },

    WALK {
        @Override
        public void behave(Entity enemy, Player player) {
            enemy.triggerPathFinding(enemy, player);
        }
    },

    CHASE {
        @Override
        public void behave(Entity enemy, Player player) {
            // different thresholds per entity (some notice you at greater distances)
            enemy.triggerChase(enemy, player);
        }
    },

    ATTACK {
        @Override
        public void behave(Entity enemy, Player player) {
            player.takeDamage(enemy.getDamage());
        }
    },

    HURT {
        @Override
        public void behave(Entity enemy, Player player) {
            // We still need to hurt the enemy and record that using takeDamage();
            // This way:
            enemy.takeDamage(player.getDamage());

            // temporary stun, knockback later
            enemy.setMoveSpeed(0);
        }
    },

    DEAD {
        @Override
        public void behave(Entity enemy, Player player) {
            enemy.setMoveSpeed(0);
            enemy.markForRemoval();
        }
    };

    public abstract void behave(Entity enemy, Player player);
}
