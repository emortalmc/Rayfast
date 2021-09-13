package dev.emortal.rayfast.util;

import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal Vector Math Utils.
 *
 * INTERNAL ONLY.
 * If any issues arise using this class, that's on you.
 */
@ApiStatus.Internal
public class VectorMathUtil {
    /**
     * Finds the distance between two vectors
     */
    public static double distance(Vector3d a, Vector3d b) {
        return Math.sqrt(distanceSquared(a, b));
    }

    public static double distanceSquared(Vector3d a, Vector3d b) {
        return Math.pow(b.x() - a.x(), 2) + Math.pow(b.y() - a.y(), 2) + Math.pow(b.z() - a.z(), 2);
    }
}
