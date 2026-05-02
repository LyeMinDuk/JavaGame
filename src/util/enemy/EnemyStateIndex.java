package util.enemy;

import java.util.Map;

public final class EnemyStateIndex {

    public static class Skeleton {
        public static final int MAX_STATE = 5;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;

        public static final Map<Integer, Integer> SKELETON_FRAME = Map.of(
                IDLE, 8,
                RUN, 10,
                ATTACK, 10,
                HURT, 5,
                DIE, 13);

    }

    public static class Shark {
        public static final int MAX_STATE = 5;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;

        public static final Map<Integer, Integer> SHARK_FRAME = Map.of(
                IDLE, 8,
                RUN, 6,
                ATTACK, 8,
                HURT, 4,
                DIE, 5);
    }

    public static class Golem {
        public static final int MAX_STATE = 5;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;

        public static final Map<Integer, Integer> GOLEM_FRAME = Map.of(
                IDLE, 8,
                RUN, 10,
                ATTACK, 11,
                HURT, 4,
                DIE, 12);
    }

    public static class DemonSlime {
        public static final int MAX_STATE = 5;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;

        public static final Map<Integer, Integer> DEMON_SLIME_FRAME = Map.of(
                IDLE, 6,
                RUN, 12,
                ATTACK, 15,
                HURT, 5,
                DIE, 22);
    }

    public static class Minotaur {
        public static final int MAX_STATE = 5;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;

        public static final Map<Integer, Integer> MINOTAUR_FRAME = Map.of(
                IDLE, 16,
                RUN, 12,
                ATTACK, 15,
                HURT, 6,
                DIE, 10);
    }

    public static class Cthulu {
        public static final int MAX_STATE = 7;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;
        public static final int FLY = 5;
        public static final int ATTACK1 = 6;

        public static final Map<Integer, Integer> CTHULU_FRAME = Map.of(
                IDLE, 15,
                RUN, 12,
                ATTACK, 16,
                HURT, 5,
                DIE, 11,
                FLY, 6,
                ATTACK1, 7);
    }

    public static class FrostGuardian {
        public static final int MAX_STATE = 5;
        public static final int IDLE = 0;
        public static final int RUN = 1;
        public static final int ATTACK = 2;
        public static final int HURT = 3;
        public static final int DIE = 4;

        public static final Map<Integer, Integer> FROST_GUARDIAN_FRAME = Map.of(
                IDLE, 6,
                RUN, 10,
                ATTACK, 14,
                HURT, 7,
                DIE, 16);
    }

}