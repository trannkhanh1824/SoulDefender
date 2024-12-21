package Enemy;

import static Helps.Constants.Enemies.PHARAON;

import Manager.EnemyManager;

public class E3 extends Enemy {

    public E3(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, PHARAON,em);
    }

}
