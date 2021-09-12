package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.area.Area;
import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.util.Converter;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Specifies an object that represents some arbitrary 3d area.
 */

public interface Area3d extends Area<Vector3d> {
    Converter<Area3d> CONVERTER = new Converter<>();

    /**
     * Returns true if the specified point is inside this area
     * @param pointX the point X
     * @param pointY the point Y
     * @param pointY the point Z
     * @return true if the point is inside this area, false otherwise
     */
    default boolean containsPoint(double pointX, double pointY, double pointZ) {
        // Find all forwards intersections
        Collection<double[]> result = lineIntersection(pointX, pointY, pointZ, 0.5, 0.5, 0.5, ALL_FORWARDS);

        // If number is odd, then return true
        assert result != null;
        return result.size() % 2 != 0;
    }

    /**
     * Returns true if the specified point is inside this area
     * @param point the point
     * @return true if the point is inside this area, false otherwise
     */
    default boolean containsPoint(@NotNull Vector3d point) {
        return containsPoint(point.x(), point.y(), point.z());
    }

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
    <R> @Nullable R lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ, @NotNull Intersection<R> intersection);

    /**
     * Returns the intersection between the specified line and this object
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return the computed line intersection position, null if none
     */
    default <R> @Nullable R lineIntersection(@NotNull Vector3d pos, @NotNull Vector3d dir, @NotNull Intersection<R> intersection) {
        return lineIntersection(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), intersection);
    }

    /**
     * Returns true if the specified line intersects this object.
     * <br><br>
     * @param posX line X position
     * @param posY line Y position
     * @param posZ line Z position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @param dirZ line Z direction
     * @return true if the line intersects this object, false otherwise
     */
    default boolean lineIntersects(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
        return lineIntersection(posX, posY, posZ, dirX, dirY, dirZ, Intersection.ANY) != null;
    }

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
     * mutability if applicable.
     * <br><br>
     * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
     *
     * @param area3ds the area3ds to take the planes from
     * @return the new CombinedArea3d or DynamicCombinedArea3d
     */
    static Area3d combined(Area3d... area3ds) {
        return new Area3dCombined(area3ds);
    }

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
     * dynamism if applicable.
     * <br><br>
     * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
     *
     * @param area3ds the area3ds to take the planes from
     * @return the new Area3d
     */
    static Area3d combined(@NotNull Collection<Area3d> area3ds) {
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
        public boolean containsPoint(double pointX, double pointY, double pointZ) {

            for (Area3d area3d : all) {
                boolean result = area3d.containsPoint(pointX, pointY, pointZ);

                if (result) {
                    return true;
                }
            }

            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> R lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ, Intersection<R> intersection) {
            Intersection.Collector<R> collector = intersection.collector();

            switch (collector.type()) {
                default:
                case ANY:
                    for (Area3d area3d : all) {
                        R result = area3d.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ, intersection);

                        if (result != null) {
                            return result;
                        }
                    }
                    return null;
                case ALL:

                    final List<double[]> list = new ArrayList<>();

                    for (Area3d area3d : all) {

                        R result = area3d.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ, intersection);

                        if (result != null) {
                            list.addAll((Collection<? extends double[]>) result);
                        }
                    }

                    return (R) list;
            }
        }
    }
}
