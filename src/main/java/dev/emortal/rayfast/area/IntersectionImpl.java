package dev.emortal.rayfast.area;

import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;

import java.util.Collection;
import java.util.List;

record IntersectionImpl<A, I>(Collector collector, Direction direction,
                              Orderer<Intersection.Result<A, I>> orderer) implements Intersection<Intersection.Result<A, I>> {

    private IntersectionImpl(BuilderImpl builder) {
        //noinspection unchecked
        this(builder.collector, builder.direction, (Orderer<Result<A, I>>) builder.orderer);
    }

    static Builder.Start builder() {
        return new BuilderStartImpl();
    }


    private static class BuilderStartImpl implements Builder.Start {
        @Override
        public Builder.Direction<Area2d, Vector2d> area2d() {
            return new BuilderImpl().direction();
        }

        @Override
        public Builder.Direction<Area3d, Vector3d> area3d() {
            return new BuilderImpl().direction();
        }

        @Override
        public Builder.Direction<Vector2d, Vector2d> vector2d() {
            return new BuilderImpl().direction();
        }

        @Override
        public Builder.Direction<Vector3d, Vector3d> vector3d() {
            return new BuilderImpl().direction();
        }
    }

    private static class BuilderImpl {

        private Collector collector = Collector.SINGLE;
        private Direction direction = Direction.ANY;
        private Orderer<?> orderer = Orderer.NONE;

        private BuilderImpl() {
        }

        private <A, I> Builder.Direction<A, I> direction() {
            return new BuilderDirection<>();
        }

        private <A, I> Builder.Collector<A, I> collector() {
            return new BuilderCollector<>();
        }

        private <R> Builder.Order<R> order() {
            return new BuilderOrder<>();
        }

        private <R> Builder.AllOrder<R> allOrder() {
            return new BuilderAllOrder<>();
        }

        private class BuilderDirection<A, I> implements Builder.Direction<A, I> {
            @Override
            public Builder.Collector<A, I> any() {
                direction = Direction.ANY;
                return collector();
            }

            @Override
            public Builder.Collector<A, I> forwards() {
                direction = Direction.FORWARDS;
                return collector();
            }

            @Override
            public Builder.Collector<A, I> backwards() {
                direction = Direction.BACKWARDS;
                return collector();
            }
        }

        private class BuilderCollector<A, I> implements Builder.Collector<A, I> {

            @Override
            public Intersection<Result<A, I>> any() {
                collector = Collector.SINGLE;
                return new IntersectionImpl<>(BuilderImpl.this);
            }

            @Override
            public Builder.Order<Result<A, I>> first() {
                // TODO: FIRST
                collector = Collector.SINGLE;
                return order();
            }

            @Override
            public Builder.Order<Result<A, I>> last() {
                // TODO: LAST
                collector = Collector.SINGLE;
                return order();
            }

            @Override
            public Builder.AllOrder<Result<A, I>> all() {
                collector = Collector.ALL;
                return allOrder();
            }
        }

        private class BuilderOrder<R> implements Builder.Order<R> {
            @Override
            public Intersection<R> distance() {
                orderer = Orderer.DISTANCE;
                //noinspection unchecked
                return (Intersection<R>) new IntersectionImpl<>(BuilderImpl.this);
            }

            @Override
            public Intersection<R> size() {
                orderer = Orderer.SIZE;
                //noinspection unchecked
                return (Intersection<R>) new IntersectionImpl<>(BuilderImpl.this);
            }
        }

        private class BuilderAllOrder<R> implements Builder.AllOrder<R> {
            @Override
            public Intersection<Collection<R>> none() {
                orderer = Orderer.NONE;
                //noinspection unchecked
                return (Intersection<Collection<R>>) (Object) new IntersectionImpl<>(BuilderImpl.this);
            }

            @Override
            public Intersection<List<R>> distance() {
                orderer = Orderer.DISTANCE;
                //noinspection unchecked
                return (Intersection<List<R>>) (Object) new IntersectionImpl<>(BuilderImpl.this);
            }

            @Override
            public Intersection<List<R>> size() {
                orderer = Orderer.SIZE;
                //noinspection unchecked
                return (Intersection<List<R>>) (Object) new IntersectionImpl<>(BuilderImpl.this);
            }
        }
    }
}
