package dev.emortal.rayfast.area.area2d;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.Intersection2dUtils;
import dev.emortal.rayfast.util.WrapperUtils;
import dev.emortal.rayfast.vector.Vector2d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a 2d polygon
 */
public interface Area2dPolygon extends Area2d {

    Map<Vector2d, Vector2d> getLines();

    @Override
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    default <R> @Nullable R lineIntersection(double posX, double posY, double dirX, double dirY, Intersection<R> intersection) {
        Intersection.Direction direction = intersection.direction();

        final double posXb = dirX + posX;
        final double posYb = dirY + posY;

        switch (intersection.collector().type()) {
            default:
            case ANY:
                for (Map.Entry<Vector2d, Vector2d> line : getLines().entrySet()) {
                    Vector2d pos1 = line.getKey();
                    Vector2d pos2 = line.getValue();

                    double[] pos = Intersection2dUtils.lineIntersection(
                            direction,

                            posX, posY,
                            posXb, posYb,

                            pos1.x(), pos1.y(),
                            pos2.x(), pos2.y()
                    );

                    if (pos != null) {
                        return (R) pos;
                    }
                }

                return null;
            case ALL:
                List<double[]> result = new ArrayList<>();

                for (Map.Entry<Vector2d, Vector2d> line : getLines().entrySet()) {
                    Vector2d pos1 = line.getKey();
                    Vector2d pos2 = line.getValue();

                    double[] pos = Intersection2dUtils.lineIntersection(
                            direction,

                            posX, posY,
                            posXb, posYb,

                            pos1.x(), pos1.y(),
                            pos2.x(), pos2.y()
                    );

                    if (pos != null) {
                        result.add(pos);
                    }
                }
                return (R) result;
        }
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a sub-optimal implementation. An ideal implementation implements the
     * Area2dPolygon interface directly on the object.
     *
     * @param object the object to wrap
     * @param linesGetter the getter for the lines
     * @param <T> the type of the wrapped object
     * @return the area that is represented by this wrapped object
     */
    static <T> Area2d wrapper(
            T object,
            WrapperUtils.LinesWrapper<T> linesGetter
    ) {
        return (Area2dPolygon) () -> linesGetter.get(object);
    }
}
