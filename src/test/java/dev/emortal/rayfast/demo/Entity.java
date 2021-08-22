package dev.emortal.rayfast.demo;

import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;

/**
 * An example usage of an entity with a bounding box.
 *
 * As the entity position changes while the bounding box stays the same, this can be used for multiple raycasts
 * consecutively with no overhead.
 *
 * This is an ideal situation for rayfast.
 */
public abstract class Entity {

    abstract BoundingBox getBoundingBox();

    abstract double getX();
    abstract double getY();
    abstract double getZ();

    class BoundingBox implements Area3dRectangularPrism {

        private final Entity entity;
        private final double halfWidth;
        private final double halfHeight;
        private final double halfDepth;

        public BoundingBox(Entity entity, double width, double height, double depth) {
            this.entity = entity;
            this.halfWidth = width / 2.0;
            this.halfHeight = height / 2.0;
            this.halfDepth = depth / 2.0;
        }

        // Coordinates
        public double getMinX() {
            return entity.getX() - halfWidth;
        }

        public double getMinY() {
            return entity.getY() - halfHeight;
        }

        public double getMinZ() {
            return entity.getZ() - halfDepth;
        }

        public double getMaxX() {
            return entity.getX() + halfWidth;
        }

        public double getMaxY() {
            return entity.getY() + halfHeight;
        }

        public double getMaxZ() {
            return entity.getZ() + halfDepth;
        }
    }
}
