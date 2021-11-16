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

    double getMinX();
    double getMinY();
    double getMaxX();
    double getMaxY();

    @SuppressWarnings("unchecked")
    default <R> @Nullable R lineIntersection(double posX, double posY, double dirX, double dirY, @NotNull Intersection<R> intersection) {
        Intersection.Direction direction = intersection.direction();


        double posXb = posX + dirX;
        double posYb = posY + dirY;


        switch (intersection.collector().type()) {
            case ANY:
                // Bottom line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMinX(), getMinY(),
                            getMaxX(), getMinY(),
                            posX, posY, posXb, posYb
                    );
                    if (vector != null) {
                        return (R) vector;
                    }
                }

                // Left line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMinX(), getMinY(),
                            getMinX(), getMaxY(),
                            posX, posY, posXb, posYb
                    );

                    if (vector != null) {
                        return (R) vector;
                    }
                }

                // Right line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMaxX(), getMinY(),
                            getMaxX(), getMaxY(),
                            posX, posY, posXb, posYb
                    );

                    if (vector != null) {
                        return (R) vector;
                    }
                }

                // Top line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMinX(), getMaxY(),
                            getMaxX(), getMaxY(),
                            posX, posY, posXb, posYb
                    );

                    if (vector != null) {
                        return (R) vector;
                    }
                }

                return null;
            case ALL:
                List<Vector2d> vectors = new ArrayList<>();
                // Bottom line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMinX(), getMinY(),
                            getMaxX(), getMinY(),
                            posX, posY, posXb, posYb
                    );
                    if (vector != null) {
                        vectors.add(vector);
                    }
                }

                // Left line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMinX(), getMinY(),
                            getMinX(), getMaxY(),
                            posX, posY, posXb, posYb
                    );

                    if (vector != null) {
                        vectors.add(vector);
                    }
                }

                // Right line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMaxX(), getMinY(),
                            getMaxX(), getMaxY(),
                            posX, posY, posXb, posYb
                    );

                    if (vector != null) {
                        vectors.add(vector);
                    }
                }

                // Top line
                {
                    Vector2d vector = Intersection2dUtils.lineIntersection(
                            direction,
                            getMinX(), getMaxY(),
                            getMaxX(), getMaxY(),
                            posX, posY, posXb, posYb
                    );

                    if (vector != null) {
                        vectors.add(vector);
                    }
                }
                return (R) vectors;
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
            public double getMinX() {
                return minXGetter.get(object);
            }

            @Override
            public double getMinY() {
                return minYGetter.get(object);
            }

            @Override
            public double getMaxX() {
                return maxXGetter.get(object);
            }

            @Override
            public double getMaxY() {
                return maxYGetter.get(object);
            }
        };
    }
}
