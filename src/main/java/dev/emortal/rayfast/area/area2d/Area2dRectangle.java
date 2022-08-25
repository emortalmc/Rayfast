package dev.emortal.rayfast.area.area2d;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.FunctionalInterfaces;
import dev.emortal.rayfast.util.Intersection2dUtils;
import dev.emortal.rayfast.vector.Vector2d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface Area2dRectangle extends Area2d {

    double minX();
    double minY();
    double maxX();
    double maxY();

    @SuppressWarnings("unchecked")
    default <R> @NotNull R lineIntersection(double posX, double posY, double dirX, double dirY, @NotNull Intersection<R> intersection) {
        Intersection.Direction direction = intersection.direction();

        double posXb = posX + dirX;
        double posYb = posY + dirY;

        switch (intersection.collector()) {
            case SINGLE -> {
                // Bottom line
                {
                    Intersection.Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            minX(), minY(),
                            maxX(), minY(),
                            posX, posY, posXb, posYb
                    );
                    if (result.intersection() != null) {
                        return (R) result;
                    }
                }

                // Left line
                {
                    Intersection.Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            minX(), minY(),
                            minX(), maxY(),
                            posX, posY, posXb, posYb
                    );

                    if (result.intersection() != null) {
                        return (R) result;
                    }
                }

                // Right line
                {
                    Intersection.Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            maxX(), minY(),
                            maxX(), maxY(),
                            posX, posY, posXb, posYb
                    );

                    if (result.intersection() != null) {
                        return (R) result;
                    }
                }

                // Top line
                {
                    Intersection.Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            minX(), maxY(),
                            maxX(), maxY(),
                            posX, posY, posXb, posYb
                    );

                    if (result.intersection() != null) {
                        return (R) result;
                    }
                }
                return (R) Intersection.Result.none(this);
            }
            case ALL -> {
                List<Intersection.@NotNull Result<Area2dRectangle, Vector2d>> results = new ArrayList<>();
                // Bottom line
                {
                    Intersection.@NotNull Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            minX(), minY(),
                            maxX(), minY(),
                            posX, posY, posXb, posYb
                    );
                    if (result.intersection() != null) {
                        results.add(result);
                    }
                }

                // Left line
                {
                    Intersection.@NotNull Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            minX(), minY(),
                            minX(), maxY(),
                            posX, posY, posXb, posYb
                    );
                    if (result.intersection() != null) {
                        results.add(result);
                    }
                }

                // Right line
                {
                    Intersection.@NotNull Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            maxX(), minY(),
                            maxX(), maxY(),
                            posX, posY, posXb, posYb
                    );
                    if (result.intersection() != null) {
                        results.add(result);
                    }
                }

                // Top line
                {
                    Intersection.@NotNull Result<Area2dRectangle, Vector2d> result = Intersection2dUtils.lineIntersection(
                            this,
                            direction,
                            minX(), maxY(),
                            maxX(), maxY(),
                            posX, posY, posXb, posYb
                    );
                    if (result.intersection() != null) {
                        results.add(result);
                    }
                }
                return (R) results;
            }
        }

        throw new IllegalStateException("Unknown intersection collector: " + intersection.collector());
    };

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a sub-optimal implementation. An ideal implementation implements the
     * Area2dRectangle interface directly on the object.
     *
     * @param object the object to wrap
     * @param minXGetter the getter for the minimum x value
     * @param minYGetter the getter for the minimum y value
     * @param maxXGetter the getter for the maximum x value
     * @param maxYGetter the getter for the maximum y value
     * @param <T> the type of the wrapped object
     * @return the area that is represented by this wrapped object
     */
    static <T> Area2d wrapper(
            T object,
            FunctionalInterfaces.DoubleWrapper<T> minXGetter,
            FunctionalInterfaces.DoubleWrapper<T> maxXGetter,
            FunctionalInterfaces.DoubleWrapper<T> minYGetter,
            FunctionalInterfaces.DoubleWrapper<T> maxYGetter
    ) {
        return new Area2dRectangle() {
            @Override
            public double minX() {
                return minXGetter.get(object);
            }

            @Override
            public double minY() {
                return minYGetter.get(object);
            }

            @Override
            public double maxX() {
                return maxXGetter.get(object);
            }

            @Override
            public double maxY() {
                return maxYGetter.get(object);
            }
        };
    }

    static @NotNull Area2dRectangle of(double minX, double minY, double maxX, double maxY) {
        return new Area2dRectangleImpl(minX, minY, maxX, maxY);
    }

    @Override
    default double area() {
        return (maxX() - minX()) * (maxY() - minY());
    }
}
