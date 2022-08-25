package dev.emortal.rayfast.area.area3d;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.util.FunctionalInterfaces;
import dev.emortal.rayfast.util.Intersection3dUtils;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A rectangular pyramid
 */
public interface Area3dRectangularPrism extends Area3d {

    // Coordinates
    double minX();
    double minY();
    double minZ();
    double maxX();
    double maxY();
    double maxZ();

    @Override
    @ApiStatus.Internal
    @SuppressWarnings("unchecked")
    default <R> @NotNull R lineIntersection(double posX, double posY, double posZ,
                                            double dirX, double dirY, double dirZ,
                                            @NotNull Intersection<R> intersection) {
        // Update and get planes
        double minX = minX();
        double minY = minY();
        double minZ = minZ();
        double maxX = maxX();
        double maxY = maxY();
        double maxZ = maxZ();

        List<Intersection.Result<Area3dRectangularPrism, Vector3d>> results = null;

        Intersection.Collector collector = intersection.collector();
        Intersection.Direction direction = intersection.direction();

        if (collector == Intersection.Collector.ALL) {
            results = new ArrayList<>();
        }

        { // Front
            Intersection.Result<Area3dRectangularPrism, Vector3d> result = Intersection3dUtils.planeIntersection(
                    this,
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

            if (result.intersection() != null) {
                switch (collector) {
                    case SINGLE: return (R) result;
                    case ALL: results.add(result);
                }
            }
        }

        { // Back
            Intersection.Result<Area3dRectangularPrism, Vector3d> result = Intersection3dUtils.planeIntersection(
                    this,
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

            if (result.intersection() != null) {
                switch (collector) {
                    case SINGLE: return (R) result;
                    case ALL: results.add(result);
                }
            }
        }

        { // Left
            Intersection.Result<Area3dRectangularPrism, Vector3d> result = Intersection3dUtils.planeIntersection(
                    this,
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

            if (result.intersection() != null) {
                switch (collector) {
                    case SINGLE: return (R) result;
                    case ALL: results.add(result);
                }
            }
        }

        { // Right
            Intersection.Result<Area3dRectangularPrism, Vector3d> result = Intersection3dUtils.planeIntersection(
                    this,
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

            if (result.intersection() != null) {
                switch (collector) {
                    case SINGLE: return (R) result;
                    case ALL: results.add(result);
                }
            }
        }

        { // Top
            Intersection.Result<Area3dRectangularPrism, Vector3d> result = Intersection3dUtils.planeIntersection(
                    this,
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

            if (result.intersection() != null) {
                switch (collector) {
                    case SINGLE: return (R) result;
                    case ALL: results.add(result);
                }
            }
        }

        { // Bottom
            Intersection.Result<Area3dRectangularPrism, Vector3d> result = Intersection3dUtils.planeIntersection(
                    this,
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

            if (result.intersection() != null) {
                switch (collector) {
                    case SINGLE: return (R) result;
                    case ALL: results.add(result);
                }
            }
        }
        if (results == null) {
            return (R) Intersection.Result.none(this);
        }

        return (R) results;
    }

    @Override
    @ApiStatus.Internal
    default <R> @NotNull R lineIntersection(@NotNull Vector3d pos, @NotNull Vector3d dir, @NotNull Intersection<R> intersection) {
        return lineIntersection(pos.x(), pos.y(), pos.z(), dir.x(), dir.y(), dir.z(), intersection);
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a suboptimal implementation. An ideal implementation implements the
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
    static <T> Area3dRectangularPrism wrapper(T object,
                              FunctionalInterfaces.DoubleWrapper<T> minXGetter,
                              FunctionalInterfaces.DoubleWrapper<T> minYGetter,
                              FunctionalInterfaces.DoubleWrapper<T> minZGetter,
                              FunctionalInterfaces.DoubleWrapper<T> maxXGetter,
                              FunctionalInterfaces.DoubleWrapper<T> maxYGetter,
                              FunctionalInterfaces.DoubleWrapper<T> maxZGetter) {
        return new Area3dRectangularPrism() {
            @Override
            public double area() {
                double minX = minX(); double minY = minY(); double minZ = minZ();
                double maxX = maxX(); double maxY = maxY(); double maxZ = maxZ();
                return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
            }

            @Override
            public double minX() {
                return minXGetter.get(object);
            }

            @Override
            public double minY() {
                return minYGetter.get(object);
            }

            @Override
            public double minZ() {
                return minZGetter.get(object);
            }

            @Override
            public double maxX() {
                return maxXGetter.get(object);
            }

            @Override
            public double maxY() {
                return maxYGetter.get(object);
            }

            @Override
            public double maxZ() {
                return maxZGetter.get(object);
            }
        };
    }

    /**
     * Generates a wrapper for the specified object using the specified getters.
     * <br><br>
     * This is a suboptimal implementation. An ideal implementation implements the
     * Area3dRectangularPrism interface directly on the object.
     *
     * @param object the object to wrap
     * @param minGetter the getter for min
     * @param maxGetter the getter for max
     * @param <T> the type of the wrapped object
     * @return the area that is represented by this wrapped object
     */
    static <T> Area3dRectangularPrism wrapper(T object,
                                              FunctionalInterfaces.Vector3dWrapper<T> minGetter,
                                              FunctionalInterfaces.Vector3dWrapper<T> maxGetter) {
        return wrapper(
                object,
                ignored -> minGetter.apply(object).x(),
                ignored -> minGetter.apply(object).y(),
                ignored -> minGetter.apply(object).z(),
                ignored -> maxGetter.apply(object).x(),
                ignored -> maxGetter.apply(object).y(),
                ignored -> maxGetter.apply(object).z()
        );
    }

    /**
     * Creates a new immutable {@link Area3dRectangularPrism} instance.
     * @param minX the minimum x value
     * @param minY the minimum y value
     * @param minZ the minimum z value
     * @param maxX the maximum x value
     * @param maxY the maximum y value
     * @param maxZ the maximum z value
     * @return the new instance
     */
    static Area3dRectangularPrism of(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new Area3dRectangularPrismImpl(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    default double area() {
        return (maxX() - minX()) * (maxY() - minY()) * (maxZ() - minZ());
    }
}
