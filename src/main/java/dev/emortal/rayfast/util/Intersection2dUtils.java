package dev.emortal.rayfast.util;


import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.vector.Vector;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Internal Intersection Utils.
 *
 * INTERNAL ONLY.
 * If any issues arise using this class, that's on you.
 */
@ApiStatus.Internal
public class Intersection2dUtils {

    private static class IncompleteResult {
        Vector2d start;
        Vector2d direction;
        @Nullable Vector2d intersection;
        @Nullable Vector2d normal;
    }

    private static final ThreadLocal<IncompleteResult> THREAD_LOCAL = ThreadLocal.withInitial(IncompleteResult::new);

    /*
    A line intersection bounded by the intersecting line points
     */

    /**
     *
     * @param direction the direction
     *
     * @param a the x of line A, point 1
     * @param b the y of line A, point 1
     * @param c the x of line A, point 2
     * @param d the y of line A, point 2
     *
     * @param e the x of line B, point 1
     * @param f the y of line B, point 1
     * @param g the x of line B, point 2
     * @param h the y of line B, point 2
     *
     * @return the intersection
     */
    @ApiStatus.Internal
    public static @NotNull <A extends Area2d> Intersection.Result<A, Vector2d> lineIntersection(
            A area,
            Intersection.Direction direction,

            // Source Line
            double a, double b,
            double c, double d,

            // Intersecting Line (Bounded)
            double e, double f,
            double g, double h
    ) {
        IncompleteResult result = THREAD_LOCAL.get();
        result.start = Vector2d.of(a, b);
        result.direction = Vector2d.of(c, d);

        // Find x & y
        double s1_x = c - a;
        double s1_y = d - b;
        double s2_x = g - e;
        double s2_y = h - f;

        double v = -s2_x * s1_y + s1_x * s2_y;
        double s = (-s1_y * (a - e) + s1_x * (b - f)) / v;
        double t = ( s2_x * (b - f) - s2_y * (a - e)) / v;

        double x = a + (t * s1_x);
        double y = b + (t * s1_y);

        if (s == Double.NEGATIVE_INFINITY || s == Double.POSITIVE_INFINITY || Double.isNaN(s) ||
            t == Double.NEGATIVE_INFINITY || t == Double.POSITIVE_INFINITY || Double.isNaN(t)) {
            return Intersection.Result.none(area);
        }

        // Find the normal
        final double normalX = -(d - b);
        final double normalY = a - c;

        // Find the distance between the start and end
        Vector2d start = result.start;
        Vector2d end = Vector2d.of(x, y);

        double distX = start.x() - end.x();
        double distY = start.y() - end.y();
        double dist = Math.sqrt(distX * distX + distY * distY);

        result.intersection = end;
        result.normal = Vector2d.of(normalX, normalY);

        // Assert that intersection was in bounds set by second line
        if (isNotBetweenUnordered(x, e, g)) {
            return Intersection.Result.none(area);
        }

        if (isNotBetweenUnordered(y, f, h)) {
            return Intersection.Result.none(area);
        }

        if (direction == Intersection.Direction.ANY) {
            return Intersection.Result.of(result.intersection, result.normal, area, dist);
        }

        // Determine if the direction is correct
        double dotProduct = (c - a) * (x - a) + (d - b) * (y - b);

        if (direction == Intersection.Direction.FORWARDS) {
            if (dotProduct < 0) {
                return Intersection.Result.none(area);
            }
        } else {
            if (dotProduct > 0) {
                return Intersection.Result.none(area);
            }
        }

        return Intersection.Result.of(result.intersection, result.normal, area, dist);
    }

    // Returns 1 if the lines intersect, otherwise 0. In addition, if the lines
// intersect the intersection point may be stored in the floats i_x and i_y.

    @ApiStatus.Internal
    private static boolean isNotBetweenUnordered(double number, double compare1, double compare2) {
        if (compare1 > compare2) {
            return !(number >= compare2) || !(number <= compare1);
        }
        return !(number >= compare1) || !(number <= compare2);
    }
}
