package dev.emortal.rayfast.util;


import dev.emortal.rayfast.area.Intersection;
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
public class Intersection3dUtils {
    private static class IncompleteResult {
        Vector3d start;
        Vector3d direction;
        @Nullable Vector3d intersection;
        @Nullable Vector3d normal;
    }

    private static final ThreadLocal<IncompleteResult> THREAD_LOCAL = ThreadLocal.withInitial(IncompleteResult::new);

    @ApiStatus.Internal
    public static @NotNull <A> Intersection.Result<A, Vector3d> planeIntersection(
            A area,
            Intersection.Direction direction,
            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Plane
            double minX, double minY, double minZ,
            double adjX, double adjY, double adjZ,
            double maxX, double maxY, double maxZ) {

        IncompleteResult result = THREAD_LOCAL.get();
        result.start = Vector3d.of(posX, posY, posZ);
        result.direction = Vector3d.of(dirX, dirY, dirZ);
        result.intersection = null;
        result.normal = null;

        getIntersection(direction, result,
                minX, minY, minZ,
                adjX, adjY, adjZ,
                maxX, maxY, maxZ);

        if (result.intersection == null) {
            return Intersection.Result.none(area);
        }

        double x = result.intersection.x();
        double y = result.intersection.y();
        double z = result.intersection.z();

        int fits = 0;

        if ((minX != maxX) && isBetweenUnordered(x, minX, maxX)) {
            fits++;
        }

        if ((minY != maxY) && isBetweenUnordered(y, minY, maxY)) {
            fits++;
        }

        if ((minZ != maxZ) && isBetweenUnordered(z, minZ, maxZ)) {
            fits++;
        }

        if (fits < 2) {
            return Intersection.Result.none(area);
        }

        Vector3d start = result.start;
        Vector3d end = result.intersection;

        // Find distance
        double distX = start.x() - end.x();
        double distY = start.y() - end.y();
        double distZ = start.z() - end.z();
        double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);

        return Intersection.Result.of(result.intersection, result.normal, area, dist);
    }

    @ApiStatus.Internal
    private static boolean isBetweenUnordered(double number, double compare1, double compare2) {
        if (compare1 > compare2) {
            return number >= compare2 && number <= compare1;
        }
        return number >= compare1 && number <= compare2;
    }

    @ApiStatus.Internal
    public static void getIntersection(
            // Line direction
            Intersection.Direction direction,
            // Result
            IncompleteResult result,
            // Plane
            double planeX, double planeY, double planeZ, // Plane point
            double planeDirX, double planeDirY, double planeDirZ // Plane normal
    ) {
        double posX = result.start.x();     double posY = result.start.y();     double posZ = result.start.z();
        double dirX = result.direction.x(); double dirY = result.direction.y(); double dirZ = result.direction.z();

        // Sensitive (speed oriented) code:
        double dotA = planeDirX * planeX + planeDirY * planeY + planeDirZ * planeZ;
        double dotB = planeDirX * posX + planeDirY * posY + planeDirZ * posZ;
        double dotC = planeDirX * dirX + planeDirY * dirY + planeDirZ * dirZ;
        double t = (dotA - dotB) / dotC;

        double x = posX + (dirX * t);
        double y = posY + (dirY * t);
        double z = posZ + (dirZ * t);

        // Get the normal vector
        double normalX = planeX - x;
        double normalY = planeY - y;
        double normalZ = planeZ - z;

        Vector3d intersection = Vector3d.of(x, y, z);
        Vector3d normal = Vector3d.of(normalX, normalY, normalZ);

        if (direction == Intersection.Direction.ANY) {
            result.intersection = intersection;
            result.normal = normal;
            return;
        }

        double dotProduct = dirX * (x - posX) + dirY * (y - posY) + dirZ * (z - posZ);

        if (direction == Intersection.Direction.FORWARDS) {
            if (dotProduct < 0) {
                return;
            }
        } else if (direction == Intersection.Direction.BACKWARDS) {
            if (dotProduct > 0) {
                return;
            }
        }

        result.intersection = intersection;
        result.normal = normal;
    }

    @ApiStatus.Internal
    public static void getIntersection(
            // Line Direction
            Intersection.Direction direction,
            // Result
            IncompleteResult result,
            // Plane
            double minX, double minY, double minZ,
            double adjX, double adjY, double adjZ,
            double maxX, double maxY, double maxZ) {

        double v1x = minX - adjX;
        double v1y = minY - adjY;
        double v1z = minZ - adjZ;
        double v2x = minX - maxX;
        double v2y = minY - maxY;
        double v2z = minZ - maxZ;

        double crossX = v1y * v2z - v2y * v1z;
        double crossY = v1z * v2x - v2z * v1x;
        double crossZ = v1x * v2y - v2x * v1y;

        getIntersection(
                // Line Direction
                direction,
                // Line
                result,
                // Plane
                minX, minY, minZ,
                crossX, crossY, crossZ
        );

        // TODO: fix this (faster) method
        /*
        System.out.println("LINE:");
        System.out.println("DIR: " + dirX + ":" + dirY + ":" + dirZ);
        System.out.println("POS: " + posX + ":" + posY + ":" + posZ);

        double ABx = maxX - minX;
        double ABy = maxY - minY;
        double ABz = maxZ - minZ;

        double ACx = adjX - minX;
        double ACy = adjY - minY;
        double ACz = adjZ - minZ;

        double perpenX = ABy * ACz - ACy * ABz;
        double perpenY = ACx * ABz - ABx * ACz;
        double perpenZ = ABx * ACy - ACx * ABy;

        // Line equation: r = pos + t * dir
        // x = posX + (dirX * t)
        // y = posY + (dirY * t)
        // z = posZ + (dirZ * t)

        // Plane equation
        // 0 = pointX(x - perpenX) + pointY(y - perpenY) + pointZ(z - perpenZ)
        // 0 = pointX(posX + (dirX * t) - perpenX) + pointY(posY + (dirY * t) - perpenY) + pointZ(posZ + (dirZ * t) - perpenZ)

        // Combine equations to find t (distance to point)
        // 0 = g(d + (a * t) - j) + h(e + (b * t) - k) + i(f + (c * t) - l)
        // t = (-dg+jg-if-eh+li+kh) / (ag+bh+ci)
        double t = (-posX * adjX + perpenX * adjX - adjZ * posZ - posY * adjY + perpenZ * adjZ + perpenY * adjY) / (dirX * adjX + dirY * adjY + dirZ * adjZ);

        // A = -B * C

        // Now use t to get the point
        // x = posX + (dirX * t)
        // y = posY + (dirY * t)
        // z = posZ + (dirZ * t)

        double x = posX + (dirX * t);
        double y = posY + (dirY * t);
        double z = posZ + (dirZ * t);
        */
    }
}
