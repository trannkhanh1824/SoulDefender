package Enemy;

import static Helps.Constants.Enemies.MONKEY;

import Manager.EnemyManager;

public class E1 extends Enemy {

    public E1(float x, float y, int ID, EnemyManager em) {
        super(x, y, ID, MONKEY,em);

    }

}
