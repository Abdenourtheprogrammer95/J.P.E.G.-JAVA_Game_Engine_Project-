package coding_project.JPEG;

public enum EnemyState {

    IDLE {
        @Override
        public void behave(Entity enemy, LivingEntity target, float delta) {
            enemy.clearMoveRequest();
        }
    },

    WALK {
        @Override
        public void behave(Entity enemy, LivingEntity target, float delta) {
            enemy.triggerPathFinding(target);
        }
    },

    CHASE {
        @Override
        public void behave(Entity enemy, LivingEntity target, float delta) {
            enemy.triggerChase(target);
        }
    },

    ATTACK {
        @Override
        public void behave(Entity enemy, LivingEntity target, float delta) {
            Monster m = (Monster) enemy;

            if (!m.canAttack()) {
                enemy.setCurrentState(EnemyState.CHASE);
                return;
            }

            if (!(target instanceof AttackTarget)) return;
            if (target.isDead()) return;

            enemy.clearMoveRequest();

            if (!enemy.isAttacking()) {
                enemy.startAttack(target);
                // ((AttackTarget) target).takeDamage(enemy.getDamage());
            }
        }
    },

    HURT {
        @Override
        public void behave(Entity enemy, LivingEntity target, float delta) {
            enemy.clearMoveRequest(); // brief stun
            enemy.updateHurt(delta); // allows for timed recovery
        }
    },

    DEAD {
        @Override
        public void behave(Entity enemy, LivingEntity target, float delta) {
            // v User-Added DEBUGGING v
            //System.out.println("[DEAD] markForRemoval");
            // ^ User-Added DEBUGGING ^

            enemy.clearMoveRequest();
        }
    };

    public abstract void behave(Entity enemy, LivingEntity target, float delta);
}
