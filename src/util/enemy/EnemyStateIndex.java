package util.enemy;

import java.util.Map;

public final class EnemyStateIndex {

    public static class Skeleton {
        public static final int MAX_STATE = 6;
        public static final int IDLE = 0;
        public static final int WALK = 1;
        public static final int ATTACK1 = 2;
        public static final int ATTACK2 = 3;
        public static final int HURT = 4;
        public static final int DIE = 5;

        public static final Map<Integer, Integer> SKELETON_FRAME = Map.of(
                IDLE, 8,
                WALK, 10,
                ATTACK1, 10,
                ATTACK2, 9,
                HURT, 5,
                DIE, 13);
                
    }

}