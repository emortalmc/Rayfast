package dev.emortal.rayfast.util;


import dev.emortal.rayfast.area.Intersection;
import org.jetbrains.annotations.ApiStatus;

/**
 * Internal Intersection Utils.
 *
 * INTERNAL ONLY.
 * If any issues arise using this class, that's on you.
 */
@ApiStatus.Internal
public class Intersection3dUtils {

    @ApiStatus.Internal
    public static double[] planeIntersection(
            Intersection.Direction direction,
            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Plane
            double minX, double minY, double minZ,
            double adjX, double adjY, double adjZ,
            double maxX, double maxY, double maxZ
    ) {
        double[] arr = getIntersection(
                direction,

                posX, posY, posZ,
                dirX, dirY, dirZ,

                minX, minY, minZ,
                adjX, adjY, adjZ,
                maxX, maxY, maxZ
        );

        double x = arr[0];
        double y = arr[1];
        double z = arr[2];

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
            return null;
        }

        return new double[] {x, y, z};
    }

    @ApiStatus.Internal
    private static boolean isBetweenUnordered(double number, double compare1, double compare2) {
        if (compare1 > compare2) {
            return number >= compare2 && number <= compare1;
        }
        return number >= compare1 && number <= compare2;
    }

    @ApiStatus.Internal
    public static double[] getIntersection(
            // Line direction
            Intersection.Direction direction,

            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Plane
            double planeX, double planeY, double planeZ, // Plane point
            double planeDirX, double planeDirY, double planeDirZ // Plane normal
    ) {
        // Sensitive (speed oriented) code:
        double dotA = planeDirX * planeX + planeDirY * planeY + planeDirZ * planeZ;
        double dotB = planeDirX * posX + planeDirY * posY + planeDirZ * posZ;
        double dotC = planeDirX * dirX + planeDirY * dirY + planeDirZ * dirZ;
        double t = (dotA - dotB) / dotC;

        double x = posX + (dirX * t);
        double y = posY + (dirY * t);
        double z = posZ + (dirZ * t);

        switch (direction) {
            default:
            case ANY: {
                return new double[] {x, y, z};
            }
            case FORWARDS: {
                // Check if position is forwards
                double dotProduct = dirX * (x - posX) + dirY * (y - posY) + dirZ * (z - posZ);

                if (dotProduct > 0) {
                    return new double[]{x, y, z};
                }
                return null;
            }
            case BACKWARDS: {
                // Check if position is forwards
                double dotProduct = dirX * (x - posX) + dirY * (y - posY) + dirZ * (z - posZ);

                if (dotProduct < 0) {
                    return new double[] {x, y, z};
                }

                return null;
            }
        }
    }

    @ApiStatus.Internal
    public static double[] getIntersection(
            // Line Direction
            Intersection.Direction direction,
            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Plane
            double minX, double minY, double minZ,
            double adjX, double adjY, double adjZ,
            double maxX, double maxY, double maxZ
    ) {

        double v1x = minX - adjX;
        double v1y = minY - adjY;
        double v1z = minZ - adjZ;
        double v2x = minX - maxX;
        double v2y = minY - maxY;
        double v2z = minZ - maxZ;

        double crossX = v1y * v2z - v2y * v1z;
        double crossY = v1z * v2x - v2z * v1x;
        double crossZ = v1x * v2y - v2x * v1y;

        return getIntersection(
                // Line Direction
                direction,
                // Line
                posX, posY, posZ,
                dirX, dirY, dirZ,
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
        return new double[] {x, y, z};
        */
    }
}
