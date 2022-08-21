package dev.emortal.rayfast.area;

import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.vector.Vector;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a filter of an Intersection
 * @param <R>
 */
public interface Intersection<R> {

    Intersection<Result<Area2d, Vector2d>> ANY_2D = Intersection.builder()
            .area2d()
            .any()
            .any();

    Intersection<Result<Area3d, Vector3d>> ANY_3D = Intersection.builder()
        .area3d()
        .any()
        .any();

    static @NotNull Builder.Start builder() {
        return IntersectionImpl.builder();
    }


    @ApiStatus.Internal
    Collector collector();
    @ApiStatus.Internal
    Direction direction();

    Orderer<R> orderer();

    @ApiStatus.Internal
    enum Collector {
        // TODO: FIRST, LAST
        SINGLE,
        ALL
    }

    @ApiStatus.Internal
    enum Direction {
        ANY,
        FORWARDS,
        BACKWARDS
    }

    interface Orderer<R> extends Comparator<R> {
        Orderer<Result<?, Vector<?>>> DISTANCE = (a, b) -> {
            double distA = a.distance();
            double distB = b.distance();
            return Double.compare(distA, distB);
        };

        Orderer<Result<Area<?>, ?>> SIZE = (a, b) -> {
            Area<?> sizeA = a.subject();
            Area<?> sizeB = b.subject();
            return Double.compare(sizeA.size(), sizeB.size());
        };

        Orderer<?> NONE = (a, b) -> 0;
    }

    interface Builder {
        interface Start {
            Direction<Area2d, Vector2d> area2d();
            Direction<Area3d, Vector3d> area3d();
            Direction<Vector2d, Vector2d> vector2d();
            Direction<Vector3d, Vector3d> vector3d();
        }

        interface Direction<A, I> {
            Collector<A, I> any();
            Collector<A, I> forwards();
            Collector<A, I> backwards();
        }

        interface Collector<A, I> {
            Intersection<Result<A, I>> any();
            Order<Result<A, I>> first();
            Order<Result<A, I>> last();

            AllOrder<Result<A, I>> all();
        }

        interface Order<R> {
            Intersection<R> distance();
            Intersection<R> size();
        }

        interface AllOrder<R> extends Order<List<R>> {
            Intersection<Collection<R>> none();
        }
    }

    /**
     * The result of an intersection.
     * @param <A> the area(s) of the intersection
     * @param <I> the intersection
     */
    interface Result<A, I> {
        static <A, I> Result<A, I> none(A area) {
            return new ResultImpl<>(null, null, area, Double.NaN);
        }

        /**
         * The intersection between the two objects.
         * @return The intersection between the two objects.
         */
        @Nullable I intersection();

        /**
         * The normal (perpendicular) object of the intersection.
         * @return The normal (perpendicular) object of the intersection.
         */
        @ApiStatus.Experimental
        I normal();

        /**
         * The subject(s) of the intersection.
         * @return The subject(s) of the intersection.
         */
        @ApiStatus.Experimental
        A subject();

        /**
         * The distance between the start of the ray and the intersection.
         * @return The distance between the start of the ray and the intersection. Double.NaN if no intersection.
         */
        double distance();

        static <A, I> Result<A, I> of(@Nullable I intersection, I normal, A subject, double distance) {
            return new ResultImpl<>(intersection, normal, subject, distance);
        }
    }
}
