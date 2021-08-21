package dev.emortal.rayfast.area.area3d;

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
    public double[][] getPlanes() {
        return planes;
    }

    @Override
    public void updatePlanes() {
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    public static double[][] generatePlanes( // TODO: Move to utils
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
