package org.emortal.rayfast.area.area3d;

import org.emortal.rayfast.util.Converter;
import org.emortal.rayfast.util.IntersectionUtils;

/**
 * Specifies an object that can represent some arbitrary 3d area.
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
     * @return
     */
    default boolean lineIntersects(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        double[][] planes = getPlanes();

        if (planes.length == 0) {
            return false;
        }

        for (double[] plane : planes) {
            boolean intersects = IntersectionUtils.forwardIntersectsPlane(
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    plane[0], plane[1], plane[2],
                    plane[3], plane[4], plane[5],
                    plane[6], plane[7], plane[8]
            );

            if (intersects) {
                return true;
            }
        }

        return false;
    };

    /**
     * Gets the planes used by this object for intersection purposes
     *
     * @return array of planes. @see IntersectionUtils#intersectPlanes
     */
    double[][] getPlanes();

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function
     *
     * @param area3ds the area3ds to take the planes from
     * @return the new combined area3d
     */
    static CombinedArea3d combined(Area3d... area3ds) {
        return new CombinedArea3d(area3ds);
    }
}
