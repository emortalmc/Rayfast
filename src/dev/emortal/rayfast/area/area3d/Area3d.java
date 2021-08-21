package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.util.Converter;
import dev.emortal.rayfast.util.IntersectionUtils;

import java.util.Collection;

/**
 * Specifies an object that represents some arbitrary 3d area.
 */

public interface Area3d {
    Converter<Area3d> CONVERTER = new Converter<>();

    /**
     * Returns true if the specified line intersects this object.
     * <br><br>
     * @param posX line X position
     * @param posY line Y position
     * @param posZ line Z position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @param dirZ line Z direction
     * @return the computed line intersection position, null if none
     */
    default double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        // Update and get planes
        if (isUpdatable()) {
            updatePlanes();
        }
        double[][] planes = getPlanes();

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

    /**
     * Gets the planes used by this object for intersection purposes
     *
     * @return array of planes. @see IntersectionUtils#intersectPlanes
     */
    double[][] getPlanes();

    /**
     * Updates the planes array object returned by {@link Area3d#getPlanes()}
     */
    void updatePlanes();

    /**
     * Gets whether this area accepts updates
     *
     * @return whether this area accepts updates
     */
    boolean isUpdatable();

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
     * dynamism if applicable.
     * <br><br>
     * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
     *
     * @param area3ds the area3ds to take the planes from
     * @return the new CombinedArea3d or DynamicCombinedArea3d
     */
    static Area3dCombined combined(Area3d... area3ds) {
        return new Area3dCombined(area3ds);
    }

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
     * dynamism if applicable.
     * <br><br>
     * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
     *
     * @param area3ds the area3ds to take the planes from
     * @return the new CombinedArea3d or DynamicCombinedArea3d
     */
    static Area3dCombined combined(Collection<Area3d> area3ds) {
        return new Area3dCombined(area3ds);
    }
}
