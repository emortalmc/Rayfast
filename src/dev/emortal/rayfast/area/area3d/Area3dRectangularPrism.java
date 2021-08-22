package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.util.IntersectionUtils;

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
}
