package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.util.Converter;

import java.util.Collection;

/**
 * Specifies an object that represents some arbitrary 3d area.
 */

public interface Area3d {
    Converter<Area3d> CONVERTER = new Converter<>();

    /**
     * Returns the intersection between the specified line and this object.
     * <br><br>
     * @param posX line X position
     * @param posY line Y position
     * @param posZ line Z position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @param dirZ line Z direction
     * @return the computed line intersection position, null if none
     */
    double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ);

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
    default boolean lineIntersects(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        return lineIntersection(posX, posY, posZ, dirX, dirY, dirZ) != null;
    };

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

    class Area3dCombined implements Area3d {

        private final Area3d[] all;

        public Area3dCombined(Area3d... area3ds) {
            this.all = area3ds;
        }

        public Area3dCombined(Collection<Area3d> area3ds) {
            this(area3ds.toArray(Area3d[]::new));
        }

        @Override
        public double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {

            for (Area3d area3d : all) {
                double[] intersection = area3d.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ);

                if (intersection != null) {
                    return intersection;
                }
            }

            return null;
        }
    }
}
