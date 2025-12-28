package coding_project.JPEG;

public interface Movable {
	default void move(Entity entity, double dx, double dy, double x_speed, double y_speed) {
		entity.setXpos((entity.getXpos() + dx) * x_speed);
		entity.setYpos((entity.getYpos() + dy) * y_speed);
	}



	void canMove();
}