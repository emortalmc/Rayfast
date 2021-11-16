package dev.emortal.rayfast.util;


import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.vector.Vector2d;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal Intersection Utils.
 *
 * INTERNAL ONLY.
 * If any issues arise using this class, that's on you.
 */
@ApiStatus.Internal
public class Intersection2dUtils {

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
    public static Vector2d lineIntersection(
            Intersection.Direction direction,

            // Source Line
            double a, double b,
            double c, double d,

            // Intersecting Line (Bounded)
            double f, double g,
            double h, double i
    ) {
        // Intersection maths
        // See: https://www.desmos.com/calculator/grkbrmrmsu

        // Find x & y
        final double x = (a * d * f - b * c * f - a * d * h + b * c * h - a * i * f + c * i * f + a * h * g - c * h * g) /
                (a * g - c * g - a * i + c * i - b * f + d * f + b * h - d * h);
        final double y = - (g * x - i * x + i * f - h * g) / (-f + h);

        // Assert that intersection was in bounds set by second line
        if (isNotBetweenUnordered(x, f, h)) {
            return null;
        }

        if (isNotBetweenUnordered(y, h, i)) {
            return null;
        }

        switch (direction) {
            default:
            case ANY: {
                return Vector2d.of(x, y);
            }
            case FORWARDS: {
                // Find dot
                double dotProduct = (c - a) * (x - a) + (d - b) * (y - b);

                if (dotProduct >= 0) {
                    return Vector2d.of(x, y);
                }

                return null;
            }
            case BACKWARDS: {
                // Find dot
                double dotProduct = (c - a) * (x - a) + (d - b) * (y - b);

                if (dotProduct <= 0) {
                    return Vector2d.of(x, y);
                }

                return null;
            }
        }
    }

    @ApiStatus.Internal
    private static boolean isNotBetweenUnordered(double number, double compare1, double compare2) {
        if (compare1 > compare2) {
            return !(number >= compare2) || !(number <= compare1);
        }
        return !(number >= compare1) || !(number <= compare2);
    }
}
