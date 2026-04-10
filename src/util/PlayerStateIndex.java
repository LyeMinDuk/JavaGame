package util;

import java.util.Map;

public class PlayerStateIndex {
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int HIT = 3;
    public static final int FALL = 4;
    public static final int ATTACK = 5;

    public static final Map<Integer, Integer> PLAYER_FRAME = Map.of(
            IDLE, 5,
            RUN, 6,
            JUMP, 3,
            HIT, 4,
            FALL, 1,
            ATTACK, 3);

}
