package dev.emortal.rayfast.area;

import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.vector.Vector;
import dev.emortal.rayfast.vector.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface Area<V extends Vector<V>> {

    ////////////////////
    // Contains point //
    ////////////////////

    // This intersection option is used to find whether the area contains this point
    Intersection<Collection<Intersection.Result<Area2d, Vector2d>>> ALL_FORWARDS = Intersection.builder()
            .area2d()
            .forwards()
            .all()
            .none();

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

    double size();
}
