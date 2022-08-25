package dev.emortal.rayfast.area.area2d;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.FunctionalInterfaces;
import dev.emortal.rayfast.util.Intersection2dUtils;
import dev.emortal.rayfast.vector.Vector2d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a 2d polygon
 */
public interface Area2dPolygon extends Area2d {

    Map<Vector2d, Vector2d> lines();

    @Override
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    default <R> @NotNull R lineIntersection(double posX, double posY, double dirX, double dirY, @NotNull Intersection<R> intersection) {
        Intersection.Direction direction = intersection.direction();
        Intersection.Orderer<Intersection.Result<Area2dPolygon, Vector2d>> orderer =
                (Intersection.Orderer<Intersection.Result<Area2dPolygon, Vector2d>>) intersection.orderer();

        final double posXb = dirX + posX;
        final double posYb = dirY + posY;

        return switch (intersection.collector()) {
            case SINGLE -> (R) lines().entrySet().stream()
                    .map(entry -> {
                        Vector2d pos1 = entry.getKey();
                        Vector2d pos2 = entry.getValue();

                        return Intersection2dUtils.lineIntersection(
                                this,
                                direction,

                                posX, posY,
                                posXb, posYb,

                                pos1.x(), pos1.y(),
                                pos2.x(), pos2.y()
                        );
                    })
                    .min(orderer)
                    .orElseGet(() -> Intersection.Result.none(this));
            case ALL -> (R) lines().entrySet()
                    .stream()
                    .map(entry -> {
                        Vector2d pos1 = entry.getKey();
                        Vector2d pos2 = entry.getValue();

                        return Intersection2dUtils.lineIntersection(
                                this,
                                direction,

                                posX, posY,
                                posXb, posYb,

                                pos1.x(), pos1.y(),
                                pos2.x(), pos2.y()
                        );
                    })
                    .sorted(orderer)
                    .collect(Collectors.toList());
        };
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a suboptimal implementation. An ideal implementation implements the
     * Area2dPolygon interface directly on the object.
     *
     * @param object the object to wrap
     * @param linesGetter the getter for the lines
     * @param <T> the type of the wrapped object
     * @return the area that is represented by this wrapped object
     */
    static <T> Area2d wrapper(T object, FunctionalInterfaces.Lines2dWrapper<T> linesGetter) {
        return (Area2dPolygon) () -> linesGetter.apply(object);
    }

    @Override
    default double area() {
        Map<Vector2d, Vector2d> lines = lines();
        double area = 0;
        for (Map.Entry<Vector2d, Vector2d> entry : lines.entrySet()) {
            Vector2d pos1 = entry.getKey();
            Vector2d pos2 = entry.getValue();
            area += pos1.x() * (pos2.y() - pos1.y()) + pos2.x() * (pos1.y() - pos2.y());
        }
        area /= 2;
        return area;
    }
}
