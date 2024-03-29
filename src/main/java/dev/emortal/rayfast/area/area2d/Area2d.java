package dev.emortal.rayfast.area.area2d;

import dev.emortal.rayfast.area.Area;
import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.Converter;
import dev.emortal.rayfast.vector.Vector2d;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Area2d extends Area<Vector2d>, Area2dLike {
    Converter<Area2d> CONVERTER = new Converter<>();

    /**
     * Returns true if the specified point is inside this area
     * @param x the point X
     * @param y the point Y
     * @return true if the point is inside this area, false otherwise
     */
    default boolean containsPoint(double x, double y) {
        // Find all intersections
        var result = lineIntersection(x, y, 0.5, 0.5, ALL_FORWARDS);

        // If number is odd, then return true
        return result.size() % 2 != 0;
    }

    @Override
    default boolean containsPoint(@NotNull Vector2d point) {
        return containsPoint(point.x(), point.y());
    }

    /**
     * Returns the intersection between the specified line and this object.
     * <br><br>
     * @param posX line X position
     * @param posY line Y position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @return the computed line intersection position, null if none
     */
    <R> @NotNull R lineIntersection(double posX, double posY, double dirX, double dirY, @NotNull Intersection<R> intersection);

    /**
     * Returns the intersection between the specified line and this object
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return the computed line intersection position, null if none
     */
    default <R> @NotNull R lineIntersection(@NotNull Vector2d pos, @NotNull Vector2d dir, @NotNull Intersection<R> intersection) {
        return lineIntersection(pos.x(), pos.y(), dir.x(), dir.y(), intersection);
    }

    /**
     * Returns true if the specified line intersects this object.
     * <br><br>
     * @param posX line X position
     * @param posY line Y position
     * @param dirX line X direction
     * @param dirY line Y direction
     * @return true if the line intersects this object, false otherwise
     */
    default boolean lineIntersects(double posX, double posY, double dirX, double dirY) {
        return lineIntersection(posX, posY, dirX, dirY, Intersection.ANY_2D).intersection() != null;
    }

    /**
     * Returns any intersection between the specified line and this area
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return any line intersection position, null if none
     */
    default @NotNull Intersection.Result<Area2d, Vector2d> lineIntersection(@NotNull Vector2d pos, @NotNull Vector2d dir) {
        return lineIntersection(pos, dir, Intersection.ANY_2D);
    }

    /**
     * Returns true if the specified line intersects this area.
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return true if the line intersects this object, false otherwise
     */
    default boolean lineIntersects(@NotNull Vector2d pos, @NotNull Vector2d dir) {
        return lineIntersection(pos, dir).intersection() != null;
    }

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
     * mutability if applicable.
     * <br><br>
     * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
     *
     * @param area2ds the area3ds to take the planes from
     * @return the new CombinedArea3d or DynamicCombinedArea3d
     */
    static Area2d combined(Area2d... area2ds) {
        return new Area2d.Area2dCombined(area2ds);
    }

    /**
     * Creates a combined area3d of all the planes contained in the areas passed to this function, accounting for
     * dynamism if applicable.
     * <br><br>
     * This method will produce a CombinedArea3d or DynamicCombinedArea3d, depending on which
     *
     * @param area2ds the area3ds to take the planes from
     * @return the new CombinedArea3d or DynamicCombinedArea3d
     */
    static Area2d combined(@NotNull Collection<Area2d> area2ds) {
        return combined(area2ds.toArray(Area2d[]::new));
    }

    class Area2dCombined implements Area2d {

        private final List<Area2d> all;

        private Area2dCombined(Area2d... area2ds) {
            this.all = List.of(area2ds);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> @NotNull R lineIntersection(double posX, double posY, double dirX, double dirY, @NotNull Intersection<R> intersection) {
            Intersection.Collector collector = intersection.collector();
            Intersection.Orderer<Intersection.Result<Area2d, Vector2d>> orderer =
                    (Intersection.Orderer<Intersection.Result<Area2d, Vector2d>>) intersection.orderer();

            return switch (collector) {
                case SINGLE -> (R) all.stream()
                        .map(area -> area.lineIntersection(posX, posY, dirX, dirY, intersection))
                        .map(result -> (Intersection.Result<Area2d, Vector2d>) result)
                        .filter(result -> result.intersection() != null)
                        .min(orderer)
                        .orElseGet(() -> Intersection.Result.none(this));
                case ALL -> (R) all.stream()
                        .map(area -> area.lineIntersection(posX, posY, dirX, dirY, intersection))
                        .map(result -> (Collection<Intersection.Result<Area2d, Vector2d>>) result)
                        .flatMap(Collection::stream)
                        .sorted(orderer)
                        .collect(Collectors.toList());
            };
        }

        @Override
        public double area() {
            // TODO: Make this work correctly, right now it is an estimate
            return all.stream().mapToDouble(Area2d::area).sum();
        }
    }

    @Override
    default @NotNull Area2d asArea2d() {
        return this;
    }
}
