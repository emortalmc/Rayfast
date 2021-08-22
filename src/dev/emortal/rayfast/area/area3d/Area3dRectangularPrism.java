package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.util.IntersectionUtils;

/**
 * A static rectangular prism class
 */
public class Area3dRectangularPrism implements Area3d {

    private final double[][] planes;

    public Area3dRectangularPrism(
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ
    ) {
        this.planes = generatePlanes(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        // Update and get planes

        if (planes.length == 0) {
            return null;
        }

        for (double[] plane : planes) {
            double[] intersection = IntersectionUtils.forwardPlaneIntersection(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    plane[0], plane[1], plane[2],
                    plane[3], plane[4], plane[5],
                    plane[6], plane[7], plane[8]
            );

            if (intersection != null) {
                return intersection;
            }
        }

        return null;
    }

    private static double[][] generatePlanes(
            double minX, double minY, double minZ,
            double maxX, double maxY, double maxZ
    ) {
        return new double[][] {
                // Front
                {
                        minX, minY, minZ,
                        minX, maxY, minZ,
                        maxX, maxY, minZ,
                },
                // Back
                {
                        minX, minY, maxZ,
                        minX, maxY, maxZ,
                        maxX, maxY, maxZ,
                },
                // Left
                {
                        minX, minY, minZ,
                        minX, maxY, minZ,
                        minX, maxY, maxZ,
                },
                // Right
                {
                        maxX, minY, minZ,
                        maxX, maxY, minZ,
                        maxX, maxY, maxZ,
                },
                // Top
                {
                        minX, maxY, minZ,
                        maxX, maxY, minZ,
                        maxX, maxY, maxZ,
                },
                // Bottom
                {
                        minX, minY, minZ,
                        maxX, minY, minZ,
                        maxX, minY, maxZ,
                }
        };
    }
}
