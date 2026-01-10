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
            if (!(enemy instanceof Monster) || !(target instanceof AttackTarget)) return;
            if (target.isDead()) {
                enemy.onAttackFinished();
                return;
            }

            enemy.clearMoveRequest();

            if (!enemy.isAttacking()) {
                enemy.startAttack(target);
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
