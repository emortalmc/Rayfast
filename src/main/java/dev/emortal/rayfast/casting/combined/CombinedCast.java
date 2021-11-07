package dev.emortal.rayfast.casting.combined;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dLike;
import dev.emortal.rayfast.casting.grid.GridCast;
import dev.emortal.rayfast.util.FunctionalInterfaces;
import dev.emortal.rayfast.util.VectorMathUtil;
import dev.emortal.rayfast.vector.Vector;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static dev.emortal.rayfast.casting.combined.CombinedCastResult.HitResult;
import static dev.emortal.rayfast.casting.combined.CombinedCastResult.HitType;
import static dev.emortal.rayfast.util.FunctionalInterfaces.Vector3dArea3dToBoolean;
import static dev.emortal.rayfast.util.FunctionalInterfaces.Vector3dToBoolean;

public class CombinedCast {

    private static final @NotNull Intersection<Vector3d> INTERSECTION_3_D_FORWARDS_ANY = Intersection.builder()
            .direction(Intersection.Direction.FORWARDS)
            .build(Intersection.Collector.ANY);

    private final double max;
    private final double min;
    private final double gridSize;
    private final boolean ordered;
    private final @Nullable FunctionalInterfaces.Vector3dArea3dToBoolean areaFunction;
    private final @Nullable Vector3dToBoolean gridFunction;

    @ApiStatus.Internal
    private CombinedCast(
            double min,
            double max,
            double gridSize,
            boolean ordered,
            @Nullable FunctionalInterfaces.Vector3dArea3dToBoolean areaFunction,
            @Nullable Vector3dToBoolean gridFunction
    ) {
        this.max = max;
        this.min = min;
        this.gridSize = gridSize;
        this.ordered = ordered;
        this.areaFunction = areaFunction;
        this.gridFunction = gridFunction;
    }

    /**
     * Applies this CombinedCast to the specified areas, using the pos and dir to specify the line.
     * @param area3ds the area3ds to intersect
     * @param pos the line pos
     * @param dir the line dir
     * @return the list of all the hit results
     */
    public @NotNull List<HitResult> apply(
            @NotNull Collection<Area3dLike> area3ds,
            @NotNull Vector3d pos,
            @NotNull Vector3d dir
    ) {
        // Cache the squared distance to remove the sqrt operation when checking distance
        double maxRange = max * max;

        List<HitResult> hitResults = new ArrayList<>();

        // Do Area3ds first
        Map<Area3d, Vector3d> area3dVector3dMap = new HashMap<>();

        maxRange = handleArea3ds(area3ds, pos, dir, area3dVector3dMap, hitResults, maxRange);

        // Now do grid cast
        handleGridCast(pos, dir, hitResults, maxRange);

        // Sort hit results if ordered
        if (ordered) {
            hitResults.sort((result1, result2) -> (int) Math.signum(result1.distanceSquared() - result2.distanceSquared()));
        }

        return hitResults;
    }

    private double handleArea3ds(
            @NotNull Collection<Area3dLike> area3ds,
            @NotNull Vector3d pos,
            @NotNull Vector3d dir,
            @NotNull Map<Area3d, Vector3d> area3dVector3dMap,
            @NotNull List<HitResult> hitResults,
            final double maxRange
    ) {

        double intermediateMaxRange = maxRange;

        // Now intersect all the area3ds
        for (Area3dLike area3dLike : area3ds) {
            // Do the deed
            Area3d area3d = area3dLike.asArea3d();

            Vector3d intersection = area3d.lineIntersection(pos, dir, INTERSECTION_3_D_FORWARDS_ANY);

            if (intersection == null) {
                continue;
            }

            // cache intersection position
            area3dVector3dMap.put(area3d, intersection);

            // Continue if areaFunction specified not to cancel
            if (areaFunction != null && !areaFunction.apply(intersection, area3d)) {
                continue;
            }

            // Update max range
            intermediateMaxRange = Math.max(min, Math.min(intermediateMaxRange, VectorMathUtil.distanceSquared(pos, intersection)));
        }

        // Now collect all hit results
        area3dVector3dMap.forEach((area3d, vector3d) -> {

            // Filter out areas beyond the max range
            final double distanceSquared = VectorMathUtil.distanceSquared(pos, vector3d);
            if (distanceSquared > maxRange) {
                return;
            }

            // Generate and add hit result
            HitResult result = new HitResult(area3d, HitType.AREA3D, vector3d, distanceSquared);

            hitResults.add(result);
        });

        return intermediateMaxRange;
    }

    private double handleGridCast(
            @NotNull Vector3d pos,
            @NotNull Vector3d dir,
            @NotNull List<HitResult> hitResults,
            final double maxRange
    ) {
        double intermediateMaxRange = maxRange;

        // Create gridcast iterator
        Iterator<Vector3d> iterator = GridCast.createExactGridIterator(pos, dir, gridSize, max);
        boolean running = true;

        // Run the iterator
        while (running && iterator.hasNext()) {
            final Vector3d vector3d = iterator.next();

            double distanceSquared = VectorMathUtil.distanceSquared(pos, vector3d);

            // Generate and add hit result
            HitResult result = new HitResult(null, HitType.GRIDUNIT, vector3d, distanceSquared);

            hitResults.add(result);

            if (gridFunction == null) {
                continue;
            }

            if (!gridFunction.apply(vector3d)) {
                continue;
            }

            // Cap max range and stop gridcast
            intermediateMaxRange = Math.max(min, distanceSquared);
            running = false;
        }

        return intermediateMaxRange;
    }

    /**
     * Returns a builder of the combined cast. This builder is used to determine the attributes of the combined cast
     * that is being built. It is advised to build CombinedCast objects once and reuse them whenever possible.
     * @return the builder
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Builder() {
        }

        private double max = Double.MAX_VALUE;
        private double min = 0;
        private double gridSize = 1.0;
        private boolean ordered = false;
        private @Nullable FunctionalInterfaces.Vector3dArea3dToBoolean areaFunction;
        private @Nullable Vector3dToBoolean gridFunction;

        /**
         * Sets the distance to terminate the cast at.
         * @param max the distance to terminate the cast at
         * @return the builder
         */
        public @NotNull Builder max(double max) {
            this.max = max;
            return this;
        }

        /**
         * Sets the minimum distance to cast to before terminating
         * @param min the minimum distance to cast to
         * @return the builder
         */
        public @NotNull Builder min(double min) {
            this.min = min;
            return this;
        }

        /**
         * Sets the function to run for every area3d intersected, which limits the combined cast to the current
         * distance if the function returns true.
         * <br><br>
         * Notes: <br>
         * This function will be run for all area3ds within the max range set in the combined cast builder. <br>
         * This function has no guaranteed order, regardless of {@link Builder#ordered(boolean)}. <br>
         * @param areaFunction the consumer to run for every area3d, returns true to limit the cast
         * @return the builder
         */
        public @NotNull Builder stopWhen(@Nullable Vector3dArea3dToBoolean areaFunction) {
            this.areaFunction = areaFunction;
            return this;
        }

        /**
         * Sets the function to run for every grid unit intersected, which limits the combined cast to the current
         * distance if the function returns true.
         * Notes: <br>
         * This function will be run for all grid units until the max range is reached, or this function returns true. <br>
         * This function has a guaranteed order, regardless of {@link Builder#ordered(boolean)}. <br>
         * @param gridFunction the function to run for every grid unit, returns true to limit the cast
         * @return the builder
         */
        public @NotNull Builder stopWhen(@Nullable Vector3dToBoolean gridFunction) {
            this.gridFunction = gridFunction;
            return this;
        }

        /**
         * Sets whether the {@link CombinedCast} result is ordered or not
         * @param ordered whether the {@link CombinedCast} result is ordered or not
         * @return the builder
         */
        public @NotNull Builder ordered(boolean ordered) {
            this.ordered = ordered;
            return this;
        }

        /**
         * Sets the grid size of this cast
         * @param gridSize the size of the gridcast units
         * @return the builder
         */
        public @NotNull Builder gridSize(double gridSize) {
            this.gridSize = gridSize;
            return this;
        }

        /**
         * Builds the {@link CombinedCast} object
         * @return the {@link CombinedCast} object
         */
        public @NotNull CombinedCast build() {
            return new CombinedCast(min, max, gridSize, ordered, areaFunction, gridFunction);
        }
    }

}
