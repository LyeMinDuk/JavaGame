package util;

import java.util.Map;

public final class EnemyStateIndex {
    private EnemyStateIndex() {}

    public static final int IDLE = 0;
    public static final int PATROL = 1;
    public static final int CHASE = 2;
    public static final int ATTACK = 3;
    public static final int HIT = 4;
    public static final int DEAD = 5;

    public static final Map<Integer, Integer> STATE_FRAME = Map.of(
        IDLE, 4,
        PATROL, 6,
        CHASE, 6,
        ATTACK, 5,
        HIT, 3,
        DEAD, 6
    );
}