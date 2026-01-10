package coding_project.JPEG;

public interface LivingEntity {
    float getXpos();
    float getYpos();
    void
    takeDamage(int damage);
    boolean isDead();

    public default float getCenterX() {
        return getXpos() + getCollisionWidth() * 0.5f;
    }

    float getCollisionWidth();

    public default float getCenterY() {
        return getYpos() + getCollisionHeight() * 0.5f;
    }

    float getCollisionHeight();
}
