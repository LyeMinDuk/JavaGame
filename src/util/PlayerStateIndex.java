package util;

import java.util.Map;

public class PlayerStateIndex {
    public static final int MAX_STATE = 8;
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int HURT = 3;
    public static final int ATTACK = 4;
    public static final int ULTIMATE = 5;
    public static final int FROZEN = 6;
    public static final int SPECIAL = 7;

    public static class KnightFrames {
        public static final Map<Integer, Integer> FRAMES = Map.of(
                IDLE, 7,
                RUN, 8,
                JUMP, 5,
                HURT, 4,
                ATTACK, 6,
                ULTIMATE, 17,
                FROZEN, 12,
                SPECIAL, 16);
    }

    public static class MageFrames {
        public static final Map<Integer, Integer> FRAMES = Map.of(
                IDLE, 8,
                RUN, 8,
                JUMP, 6,
                HURT, 4,
                ATTACK, 7,
                ULTIMATE, 16,
                FROZEN, 12,
                SPECIAL, 9);
    }

}