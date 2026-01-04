package coding_project.JPEG;

public interface Movable {
	default void move(Entity entity, float dx, float dy, float moveSpeed) {
        entity.setXpos(entity.getXpos() + dx * moveSpeed);
        entity.setYpos(entity.getYpos() + dy * moveSpeed);
	}

	void canMove();
}
