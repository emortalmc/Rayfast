package dev.emortal.rayfast.area;

import dev.emortal.rayfast.vector.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Area<V extends Vector> {

    ////////////////////
    // Contains point //
    ////////////////////

    // This intersection option is used to find whether the area contains this point
    Intersection<Collection<double[]>> ALL_FORWARDS = Intersection.builder()
            .direction(Intersection.Direction.FORWARDS)
            .build(Intersection.Collector.ALL);

    /**
     * Returns true if the specified point is inside this area
     * @param point the point
     * @return true if the point is inside this area, false otherwise
     */
    boolean containsPoint(@NotNull V point);

    ///////////////////////
    // Line intersection //
    ///////////////////////

    /**
     * Returns the specified intersection result if a line intersection was done with this area.
     *
     * @param pos the start position of the line
     * @param dir the direction of the line
     * @param intersection the specified intersection result to return
     * @param <R> the intersection result type
     * @return the intersection result
     */
    <R> @Nullable R lineIntersection(@NotNull V pos, @NotNull V dir, @NotNull Intersection<R> intersection);

    /**
     * Returns any intersection between the specified line and this area
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return any line intersection position, null if none
     */
    default double @Nullable [] lineIntersection(@NotNull V pos, @NotNull V dir) {
        return lineIntersection(pos, dir, Intersection.ANY);
    };

    /**
     * Returns true if the specified line intersects this area.
     * <br><br>
     * @param pos line position
     * @param dir line direction
     * @return true if the line intersects this object, false otherwise
     */
    default boolean lineIntersects(@NotNull V pos, @NotNull V dir) {
        return lineIntersection(pos, dir) != null;
    };
}
