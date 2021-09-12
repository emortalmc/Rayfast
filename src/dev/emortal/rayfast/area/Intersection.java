package dev.emortal.rayfast.area;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Represents a filter of an Intersection
 * @param <R>
 */
final public class Intersection<R> {

    public static Intersection.Builder builder() {
        return new Builder();
    }

    public static final Intersection<double[]> ANY = builder()
            .direction(Direction.ANY)
            .build(Collector.ANY);

    private final Direction direction;
    private final Collector<R> collector;

    private Intersection(Direction direction, Collector<R> collector) {
        this.direction = direction;
        this.collector = collector;
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

        public static final Collector<double[]> ANY = new Collector<>(Type.ANY);
        public static final Collector<Collection<double[]>> ALL = new Collector<>(Type.ALL);

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

        public <R> @NotNull Intersection<R> build(@NotNull Collector<R> collector) {
            return new Intersection<>(direction, collector);
        }

    }
}
