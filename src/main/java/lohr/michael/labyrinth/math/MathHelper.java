package lohr.michael.labyrinth.math;

/**
 * Created by Michael Lohr on 13.01.2016.
 *
 * contains some useful math operations
 */
public class MathHelper {

    // clamps a value to a min and max value
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    // clamps a value to a min and max value
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    // clamps between 0, 1
    public static int clamp01(int value) {
        return clamp(value, 0, 1);
    }

    // clamps between 0, 1
    public static float clamp01(float value) {
        return clamp(value, 0, 1);
    }

    // clamps between -1, 1
    public static int clamp11(int value) {
        return clamp(value, -1, 1);
    }

    // clamps between -1, 1
    public static float clamp11(float value) {
        return clamp(value, -1, 1);
    }

    // distance between two vectors
    public static float distance(Position one, Position two) {
        return one.subtract(two).length();
    }

}
