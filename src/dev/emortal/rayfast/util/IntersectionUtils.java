package dev.emortal.rayfast.util;

/**
 * Internal Intersection Utils.
 *
 * INTERNAL ONLY.
 * If any issues arise using this class, that's on you.
 */
@Deprecated(forRemoval = false)
public class IntersectionUtils {

    public static double[] forwardPlaneIntersection(
            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Plane
            double minX, double minY, double minZ,
            double adjX, double adjY, double adjZ,
            double maxX, double maxY, double maxZ
    ) {
        double[] pos = planeIntersection(
                posX, posY, posZ,
                dirX, dirY, dirZ,
                minX, minY, minZ,
                adjX, adjY, adjZ,
                maxX, maxY, maxZ
        );

        if (pos == null) {
            return null;
        }

        // Check if position is forwards
        double dotProduct = dirX * (pos[0] - posX) + dirY * (pos[1] - posY) + dirZ * (pos[2] - posZ);

        if (dotProduct > 0) {
            return pos;
        }

        return null;
    }

    public static double[] planeIntersection(
            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Plane
            double minX, double minY, double minZ,
            double adjX, double adjY, double adjZ,
            double maxX, double maxY, double maxZ
    ) {
        double[] arr = getIntersection(
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


    public static boolean isBetween(double number, double min, double max) {
        return number >= min && number <= max;
    }

    public static boolean isBetweenUnordered(double number, double compare1, double compare2) {
        if (compare1 > compare2) {
            return isBetween(number, compare2, compare1);
        }
        return isBetween(number, compare1, compare2);
    }

    /**
     * Gets the intersections of the specified line with the specified planes
     *
     * @param dirX   Line Direction X
     * @param dirY   Line Direction Y
     * @param dirZ   Line Direction Z
     * @param posX   Line Position X
     * @param posY   Line Position Y
     * @param posZ   Line Position Z
     * @param planes   Array of planes. Each plane contains 3 X, Y, and Z coordinates, with 9 elements in total. Example:
     * @<code>
     * {
     *   {
     *     1.3, -5.9, 3.0,
     *     1.1, -5.3, 3.4,
     *     0.3, -3.9, 3.2,
     *   }
     * }
     * </code>
     * @return Array of intersection positions.
     */
    public static double[][] intersectPlanes(
            // Line
            double posX, double posY, double posZ, // Position vector
            double dirX, double dirY, double dirZ, // Direction vector
            // Planes
            double[]... planes
    ) {
        double[][] positions = new double[planes.length][3];

        for (int i = 0; i < planes.length; i++) {

            double[] plane = planes[i];

            // Get intersection and add to array
            double[] position = getIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    plane[0], plane[1], plane[2],
                    plane[3], plane[4], plane[5],
                    plane[6], plane[7], plane[8]
            );

            positions[i] = position;
        }

        return positions;
    }

    public static double[] getIntersection(
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

        return new double[] {x, y, z};
    }

    private static double getDot(
            double x, double y, double z,
            double vecX, double vecY, double vecZ
    ) {
        return x * vecX + y * vecY + z * vecZ;
    }

    public static double[] getIntersection(
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
