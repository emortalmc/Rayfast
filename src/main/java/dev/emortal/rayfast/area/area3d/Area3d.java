package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.area.Area;
import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.Converter;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specifies an object that represents some arbitrary 3d area.
 */

public interface Area3d extends Area<Vector3d>, Area3dLike {
    Converter<Area3d> CONVERTER = new Converter<>();

    /**
     * Returns true if the specified point is inside this area
     * @param pointX the point X
     * @param pointY the point Y
     * @param pointZ the point Z
     * @return true if the point is inside this area, false otherwise
     */
    default boolean containsPoint(double pointX, double pointY, double pointZ) {
        // Find all forwards intersections
        var result = lineIntersection(pointX, pointY, pointZ, 0.5, 0.5, 0.5, ALL_FORWARDS);

        // If number is odd, then return true
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
    <R> @NotNull R lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ, @NotNull Intersection<R> intersection);

    /**
     * Returns the intersection between the specified line and this object
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return the computed line intersection position, null if none
     */
    default <R> @NotNull R lineIntersection(@NotNull Vector3d pos, @NotNull Vector3d dir, @NotNull Intersection<R> intersection) {
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
        return lineIntersection(posX, posY, posZ, dirX, dirY, dirZ, Intersection.ANY_3D).intersection() != null;
    }

    /**
     * Returns any intersection between the specified line and this area
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return any line intersection position, null if none
     */
    default @Nullable Vector3d lineIntersection(@NotNull Vector3d pos, @NotNull Vector3d dir) {
        return lineIntersection(pos, dir, Intersection.ANY_3D).intersection();
    }

    /**
     * Returns true if the specified line intersects this area.
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return true if the line intersects this object, false otherwise
     */
    default boolean lineIntersects(@NotNull Vector3d pos, @NotNull Vector3d dir) {
        return lineIntersection(pos, dir) != null;
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

        private final List<Area3d> all;

        public Area3dCombined(Area3d... area3ds) {
            this.all = List.of(area3ds);
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
        public <R> @NotNull R lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ, @NotNull Intersection<R> intersection) {
            Intersection.Collector collector = intersection.collector();
            Intersection.Orderer<Intersection.Result<Area3d, Vector3d>> orderer =
                    (Intersection.Orderer<Intersection.Result<Area3d, Vector3d>>) intersection.orderer();

            return switch (collector) {
                case SINGLE -> (R) all.stream()
                        .map(area -> area.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ, intersection))
                        .map(result -> (Intersection.Result<Area3d, Vector3d>) result)
                        .filter(result -> result.intersection() != null)
                        .min(orderer)
                        .orElseGet(() -> Intersection.Result.none(this));
                case ALL -> (R) all.stream()
                        .map(area -> area.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ, intersection))
                        .map(result -> (Collection<Intersection.Result<Area3d, Vector3d>>) result)
                        .flatMap(Collection::stream)
                        .sorted(orderer)
                        .collect(Collectors.toList());
            };
        }

        @Override
        public double size() {
            // TODO: Make this work correctly, right now it is an estimate
            return all.stream().mapToDouble(Area3d::size).sum();
        }
    }

    @Override
    default @NotNull Area3d asArea3d() {
        return this;
    }
}
