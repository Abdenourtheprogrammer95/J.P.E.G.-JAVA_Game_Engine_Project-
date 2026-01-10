package coding_project.JPEG;

public interface Movable {
    @Deprecated
    default void move(Entity entity, float dx, float dy, float delta) {
        System.out.println(
            "[MOVE] frame=" + com.mygdx.game.GameScreenDebug.frame +
                " entity=" + entity.getClass().getSimpleName() +
                " dx=" + dx +
                " dy=" + dy +
                " speed=" + entity.getMoveSpeed() +
                " BEFORE=(" + entity.getXpos() + "," + entity.getYpos() + ")" +
                " caller=" + Thread.currentThread().getStackTrace()[2].getMethodName()
        );

        entity.setXpos(entity.getXpos() + dx * entity.getMoveSpeed() * delta);
        entity.setYpos(entity.getYpos() + dy * entity.getMoveSpeed() * delta);

        System.out.println(
            "[MOVE] AFTER=(" + entity.getXpos() + "," + entity.getYpos() + ")"
        );
    }

    @Deprecated
    boolean canMove();
}
