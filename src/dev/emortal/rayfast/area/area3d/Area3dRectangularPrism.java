package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.util.IntersectionUtils;

import java.util.function.Function;

/**
 * A static rectangular prism class
 */
public interface Area3dRectangularPrism extends Area3d {

    // Coordinates
    double getMinX();
    double getMinY();
    double getMinZ();
    double getMaxX();
    double getMaxY();
    double getMaxZ();


    @Override
    default double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        // Update and get planes
        double minX = getMinX();
        double minY = getMinY();
        double minZ = getMinZ();
        double maxX = getMaxX();
        double maxY = getMaxY();
        double maxZ = getMaxZ();

        { // Front
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, minZ,
                    minX, maxY, minZ,
                    maxX, maxY, minZ
            );

            if (intersection != null) {
                return intersection;
            }
        }

        { // Back
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, maxZ,
                    minX, maxY, maxZ,
                    maxX, maxY, maxZ
            );

            if (intersection != null) {
                return intersection;
            }
        }

        { // Left
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, minZ,
                    minX, maxY, minZ,
                    minX, maxY, maxZ
            );

            if (intersection != null) {
                return intersection;
            }
        }

        { // Right
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    maxX, minY, minZ,
                    maxX, maxY, minZ,
                    maxX, maxY, maxZ
            );

            if (intersection != null) {
                return intersection;
            }
        }

        { // Top
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, maxY, minZ,
                    maxX, maxY, minZ,
                    maxX, maxY, maxZ
            );

            if (intersection != null) {
                return intersection;
            }
        }

        { // Bottom
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, minZ,
                    maxX, minY, minZ,
                    maxX, minY, maxZ
            );

            if (intersection != null) {
                return intersection;
            }
        }

        return null;
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a sub-optimal implementation due to java generic limitations. An ideal implementation implements the
     * Area3dRectangularPrism interface directly on the object.
     *
     * @param object the object to wrap
     * @param minXGetter the getter for minX
     * @param minYGetter the getter for minY
     * @param minZGetter the getter for minZ
     * @param maxXGetter the getter for maxX
     * @param maxYGetter the getter for maxY
     * @param maxZGetter the getter for maxZ
     * @param <T> the type of the wrapper
     * @return the area that is represented by this wrapped
     */
    static <T> Area3dRectangularPrism wrapper(
            T object,
            Function<T, Double> minXGetter,
            Function<T, Double> minYGetter,
            Function<T, Double> minZGetter,
            Function<T, Double> maxXGetter,
            Function<T, Double> maxYGetter,
            Function<T, Double> maxZGetter
    ) {
        return new Area3dRectangularPrism() {
            @Override
            public double getMinX() {
                return minXGetter.apply(object);
            }

            @Override
            public double getMinY() {
                return minYGetter.apply(object);
            }

            @Override
            public double getMinZ() {
                return minZGetter.apply(object);
            }

            @Override
            public double getMaxX() {
                return maxXGetter.apply(object);
            }

            @Override
            public double getMaxY() {
                return maxYGetter.apply(object);
            }

            @Override
            public double getMaxZ() {
                return maxZGetter.apply(object);
            }
        };
    }
}
