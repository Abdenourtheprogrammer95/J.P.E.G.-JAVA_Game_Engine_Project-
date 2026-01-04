package coding_project.JPEG;

public class Wave {
    private final int numEnemies;
    private final boolean aggro;
    private final Class<? extends Entity> enemyType; // store the class

    public Wave(int numEnemies, boolean aggro, Class<? extends Entity> enemyType) {
        this.numEnemies = numEnemies;
        this.aggro = aggro;
        this.enemyType = enemyType;
    }

    public int getNumEnemies() {
        return numEnemies;
    }

    public boolean isAggro() {
        return aggro;
    }

    public Class<? extends Entity> getEnemyType() {
        return enemyType;
    }
}
