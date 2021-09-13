package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.FunctionalInterfaces;
import dev.emortal.rayfast.util.Intersection3dUtils;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular pyramid
 */
public interface Area3dRectangularPrism extends Area3d {

    // Coordinates
    double getMinX();
    double getMinY();
    double getMinZ();
    double getMaxX();
    double getMaxY();
    double getMaxZ();

    @Override
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    default <R> @Nullable R lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ, @NotNull Intersection<R> intersection) {
        // Update and get planes
        double minX = getMinX();
        double minY = getMinY();
        double minZ = getMinZ();
        double maxX = getMaxX();
        double maxY = getMaxY();
        double maxZ = getMaxZ();

        // Don't initialize this collection until we know that we need to collect the values.
        List<Vector3d> result = null;

        Intersection.Collector.Type collectorType = intersection.collector().type();
        Intersection.Direction direction = intersection.direction();

        if (collectorType == Intersection.Collector.Type.ALL) {
            result = new ArrayList<>();
        }

        { // Front
            Vector3d pos = Intersection3dUtils.planeIntersection(
                    // Line direction
                    direction,
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, minZ,
                    minX, maxY, minZ,
                    maxX, maxY, minZ
            );

            if (pos != null) {
                switch (collectorType) {
                    default:
                    case ANY:
                        return (R) pos;
                    case ALL:
                        result.add(pos);
                }
            }
        }

        { // Back
            Vector3d pos = Intersection3dUtils.planeIntersection(
                    // Line direction
                    direction,
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, maxZ,
                    minX, maxY, maxZ,
                    maxX, maxY, maxZ
            );

            if (pos != null) {
                switch(collectorType) {
                    default:
                    case ANY:
                        return (R) pos;
                    case ALL:
                        result.add(pos);
                }
            }
        }

        { // Left
            Vector3d pos = Intersection3dUtils.planeIntersection(
                    // Line direction
                    direction,
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, minZ,
                    minX, maxY, minZ,
                    minX, maxY, maxZ
            );

            if (pos != null) {
                switch(collectorType) {
                    default:
                    case ANY:
                        return (R) pos;
                    case ALL:
                        result.add(pos);
                }
            }
        }

        { // Right
            Vector3d pos = Intersection3dUtils.planeIntersection(
                    // Line direction
                    direction,
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    maxX, minY, minZ,
                    maxX, maxY, minZ,
                    maxX, maxY, maxZ
            );

            if (pos != null) {
                switch(collectorType) {
                    default:
                    case ANY:
                        return (R) pos;
                    case ALL:
                        result.add(pos);
                }
            }
        }

        { // Top
            Vector3d pos = Intersection3dUtils.planeIntersection(
                    // Line direction
                    direction,
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, maxY, minZ,
                    maxX, maxY, minZ,
                    maxX, maxY, maxZ
            );

            if (pos != null) {
                switch(collectorType) {
                    default:
                    case ANY:
                        return (R) pos;
                    case ALL:
                        result.add(pos);
                }
            }
        }

        { // Bottom
            Vector3d pos = Intersection3dUtils.planeIntersection(
                    // Line direction
                    direction,
                    // Line
                    posX, posY, posZ,
                    dirX, dirY, dirZ,
                    // Plane
                    minX, minY, minZ,
                    maxX, minY, minZ,
                    maxX, minY, maxZ
            );

            if (pos != null) {
                switch(collectorType) {
                    default:
                    case ANY:
                        return (R) pos;
                    case ALL:
                        result.add(pos);
                }
            }
        }

        return (R) result;
    }

    @Override
    @ApiStatus.Internal
    default <R> @Nullable R lineIntersection(@NotNull Vector3d pos, @NotNull Vector3d dir, @NotNull Intersection<R> intersection) {
        return lineIntersection(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), intersection);
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a sub-optimal implementation. An ideal implementation implements the
     * Area3dRectangularPrism interface directly on the object.
     *
     * @param object the object to wrap
     * @param minXGetter the getter for minX
     * @param minYGetter the getter for minY
     * @param minZGetter the getter for minZ
     * @param maxXGetter the getter for maxX
     * @param maxYGetter the getter for maxY
     * @param maxZGetter the getter for maxZ
     * @param <T> the type of the wrapped object
     * @return the area that is represented by this wrapped object
     */
    static <T> Area3d wrapper(
            T object,
            FunctionalInterfaces.DoubleWrapper<T> minXGetter,
            FunctionalInterfaces.DoubleWrapper<T> minYGetter,
            FunctionalInterfaces.DoubleWrapper<T> minZGetter,
            FunctionalInterfaces.DoubleWrapper<T> maxXGetter,
            FunctionalInterfaces.DoubleWrapper<T> maxYGetter,
            FunctionalInterfaces.DoubleWrapper<T> maxZGetter
    ) {
        return new Area3dRectangularPrism() {
            @Override
            public double getMinX() {
                return minXGetter.get(object);
            }

            @Override
            public double getMinY() {
                return minYGetter.get(object);
            }

            @Override
            public double getMinZ() {
                return minZGetter.get(object);
            }

            @Override
            public double getMaxX() {
                return maxXGetter.get(object);
            }

            @Override
            public double getMaxY() {
                return maxYGetter.get(object);
            }

            @Override
            public double getMaxZ() {
                return maxZGetter.get(object);
            }
        };
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a sub-optimal implementation. An ideal implementation implements the
     * Area3dRectangularPrism interface directly on the object.
     *
     * @param object the object to wrap
     * @param minGetter the getter for min
     * @param maxGetter the getter for max
     * @param <T> the type of the wrapped object
     * @return the area that is represented by this wrapped object
     */
    static <T> Area3dRectangularPrism wrapper(
            T object,
            FunctionalInterfaces.Vector3dWrapper<T> minGetter,
            FunctionalInterfaces.Vector3dWrapper<T> maxGetter
    ) {
        return new Area3dRectangularPrism() {
            @Override
            public double getMinX() {
                return minGetter.get(object).x();
            }

            @Override
            public double getMinY() {
                return minGetter.get(object).y();
            }

            @Override
            public double getMinZ() {
                return minGetter.get(object).z();
            }

            @Override
            public double getMaxX() {
                return maxGetter.get(object).x();
            }

            @Override
            public double getMaxY() {
                return maxGetter.get(object).y();
            }

            @Override
            public double getMaxZ() {
                return maxGetter.get(object).z();
            }
        };
    }
}
