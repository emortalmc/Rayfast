package dev.emortal.rayfast.area.area3d;

public class Area3dRectangularPrism implements Area3d {

    // Coordinates
    private final double minX;
    private final double minY;
    private final double minZ;

    private final double maxX;
    private final double maxY;
    private final double maxZ;

    // Planes
    private final double[][] planes;

    public Area3dRectangularPrism(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.planes = generatePlanes();
    }

    @Override
    public double[][] getPlanes() {
        return planes;
    }

    private double[][] generatePlanes() {
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
