package dev.emortal.rayfast.area;

import dev.emortal.rayfast.vector.Vector;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents a filter of an Intersection
 * @param <R>
 */
final public class Intersection<R> {

    public static final Intersection<Vector2d> ANY_2D = Intersection.builder()
            .direction(Direction.ANY)
            .build(Collector.ANY);

    public static final Intersection<Vector3d> ANY_3D = Intersection.builder()
            .direction(Direction.ANY)
            .build(Collector.ANY);

    public static Intersection.Builder builder() {
        return new Builder();
    }

    private final Direction direction;
    private final Collector<R> collector;

    @SuppressWarnings("unchecked")
    private Intersection(Direction direction, Collector<?> collector) {
        this.direction = direction;
        this.collector = (Collector<R>) collector;
    }

    public Direction direction() {
        return direction;
    }

    public Collector<R> collector() {
        return collector;
    }

    public enum Direction {
        ANY,
        FORWARDS,
        BACKWARDS
    }

    public static class Collector<R> {
        private final Type type;
        private Collector(Type type) {
            this.type = type;
        }

        public Type type() {
            return type;
        }

        public static final Collector<? extends Vector> ANY = new Collector<>(Type.ANY);
        public static final Collector<Collection<? extends Vector>> ALL = new Collector<>(Type.ALL);

        // Enum used to switch on
        public enum Type {
            ANY,
            ALL
        }
    }

    public static class Builder {
        private Builder() {
        }

        private Direction direction = Direction.ANY;

        public @NotNull Builder direction(@NotNull Direction direction) {
            this.direction = direction;
            return this;
        }

        public <R> @NotNull Intersection<R> build(@NotNull Collector<?> collector) {
            return new Intersection<>(direction, collector);
        }
    }
}
