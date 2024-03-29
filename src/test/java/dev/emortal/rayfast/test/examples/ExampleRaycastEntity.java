package dev.emortal.rayfast.test.examples;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dLike;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import org.jetbrains.annotations.NotNull;

/**
 * An example usage of an entity with a bounding box.
 * <p>
 * As the entity position changes while the bounding box stays the same, this can be used for multiple raycasts
 * consecutively with no overhead.
 * <p>
 * This is an ideal situation for mutable shapes in rayfast.
 */
public abstract class ExampleRaycastEntity implements Area3dLike {

    public abstract BoundingBox getBoundingBox();

    @Override
    public @NotNull Area3d asArea3d() {
        return getBoundingBox();
    }

    public abstract double getX();
    public abstract double getY();
    public abstract double getZ();

    public static class BoundingBox implements Area3dRectangularPrism {

        private final ExampleRaycastEntity exampleRaycastEntity;
        private final double halfWidth;
        private final double halfHeight;
        private final double halfDepth;

        public BoundingBox(ExampleRaycastEntity exampleRaycastEntity, double width, double height, double depth) {
            this.exampleRaycastEntity = exampleRaycastEntity;
            this.halfWidth = width / 2.0;
            this.halfHeight = height / 2.0;
            this.halfDepth = depth / 2.0;
        }

        // Coordinates
        public double minX() {
            return exampleRaycastEntity.getX() - halfWidth;
        }

        public double minY() {
            return exampleRaycastEntity.getY() - halfHeight;
        }

        public double minZ() {
            return exampleRaycastEntity.getZ() - halfDepth;
        }

        public double maxX() {
            return exampleRaycastEntity.getX() + halfWidth;
        }

        public double maxY() {
            return exampleRaycastEntity.getY() + halfHeight;
        }

        public double maxZ() {
            return exampleRaycastEntity.getZ() + halfDepth;
        }
    }
}
