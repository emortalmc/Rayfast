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
     * @param f the x of line B, point 1
     * @param g the y of line B, point 1
     * @param h the x of line B, point 2
     * @param i the y of line B, point 2
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
            double f, double g,
            double h, double i
    ) {
        IncompleteResult result = THREAD_LOCAL.get();
        result.start = Vector2d.of(a, b);
        result.direction = Vector2d.of(c, d);

        // Intersection maths
        // See: https://www.desmos.com/calculator/grkbrmrmsu

        // Find x & y
        final double x = (a * d * f - b * c * f - a * d * h + b * c * h - a * i * f + c * i * f + a * h * g - c * h * g) /
                (a * g - c * g - a * i + c * i - b * f + d * f + b * h - d * h);
        final double y = - (g * x - i * x + i * f - h * g) / (-f + h);

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
        if (isNotBetweenUnordered(x, f, h)) {
            return Intersection.Result.none(area);
        }

        if (isNotBetweenUnordered(y, h, i)) {
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

    @ApiStatus.Internal
    private static boolean isNotBetweenUnordered(double number, double compare1, double compare2) {
        if (compare1 > compare2) {
            return !(number >= compare2) || !(number <= compare1);
        }
        return !(number >= compare1) || !(number <= compare2);
    }
}
