package dev.emortal.rayfast.test;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.area.area2d.Area2dPolygon;
import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dLike;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.casting.combined.CombinedCast;
import dev.emortal.rayfast.casting.grid.GridCast;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Performance tests.
 */
public class RunBenchmarks {

    private static Area3d interfacedCombined;
    private static Area3d wrapperCombined;
    private static Area2d polygonTests;

    private static Collection<Area3dLike> combinedCastAreas;

    public static void main(String[] args) {
        // Register entity to rayfast converter
        Area3d.CONVERTER.register(dev.emortal.rayfast.test.ExampleRaycastEntity.class, ExampleRaycastEntity::getBoundingBox);

        long startMillis = System.currentTimeMillis();
        System.out.println("Setting up Area3ds");

        Area3d[] interfaced = new Area3d[1000];

        // Create new entity
        ExampleRaycastEntity exampleRaycastEntity = new ExampleRaycastEntity() {
            private final BoundingBox box = new BoundingBox(this, 123, 456, 789);

            @Override
            public BoundingBox getBoundingBox() {
                return box;
            }

            @Override
            public double getX() {
                return Math.random();
            }

            @Override
            public double getY() {
                return Math.random();
            }

            @Override
            public double getZ() {
                return Math.random();
            }
        };

        Arrays.fill(interfaced, Area3d.CONVERTER.from(exampleRaycastEntity));

        interfacedCombined = Area3d.combined(interfaced);


        Area3d[] wrappers = new Area3d[1000];

        Arrays.fill(wrappers, Area3dRectangularPrism.wrapper(
                exampleRaycastEntity.getBoundingBox(),
                ExampleRaycastEntity.BoundingBox::getMinX,
                ExampleRaycastEntity.BoundingBox::getMinY,
                ExampleRaycastEntity.BoundingBox::getMinZ,
                ExampleRaycastEntity.BoundingBox::getMaxX,
                ExampleRaycastEntity.BoundingBox::getMaxY,
                ExampleRaycastEntity.BoundingBox::getMaxZ
        ));

        wrapperCombined = Area3d.combined(wrappers);

        Area2d[] polygons = new Area2d[1000];

        Vector2d pos1 = Vector2d.of(Math.random(), Math.random());
        Vector2d pos2 = Vector2d.of(Math.random(), Math.random());
        Vector2d pos3 = Vector2d.of(Math.random(), Math.random());

        final Map<Vector2d, Vector2d> lines = Map.of(
                pos1, pos2,
                pos2, pos3,
                pos3, pos1
        );

        Arrays.fill(polygons, (Area2dPolygon) () -> lines);

        polygonTests = Area2d.combined(polygons);

        System.out.println("Finished after " + (System.currentTimeMillis() - startMillis) + "ms");

        combinedCastAreas = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            double minX = Math.random(); double maxX = Math.random();
            double minY = Math.random(); double maxY = Math.random();
            double minZ = Math.random(); double maxZ = Math.random();

            combinedCastAreas.add(new Area3dRectangularPrism() {
                @Override
                public double getMinX() {
                    return minX;
                }

                @Override
                public double getMinY() {
                    return minY;
                }

                @Override
                public double getMinZ() {
                    return minZ;
                }

                @Override
                public double getMaxX() {
                    return maxX;
                }

                @Override
                public double getMaxY() {
                    return maxY;
                }

                @Override
                public double getMaxZ() {
                    return maxZ;
                }
            });
        }

        benchmarkArea2d();
        benchmarkArea2d();
        benchmarkBlocks();
        benchmarkCombinedCast();
    }

    private static void benchmarkBlocks() {
        long startMillis = System.currentTimeMillis();

        Iterable<Vector3d> iterable = GridCast.createGridIterator(
                Math.random(), Math.random(), Math.random(),
                Math.random(), Math.random(), Math.random(),
                1,
                100_000
        );

        int i = 0;

        for (Vector3d ignored : iterable) {
            i++;
        }

        System.out.println("took " + (System.currentTimeMillis() - startMillis) + "ms to iterate over " + i + " grid units (1x1 cubes)");
    }

    private static void benchmarkArea3d() {

        final Intersection<Vector3d> intersection = Intersection.ANY.cast();

        {
            long millis = System.currentTimeMillis();

            for (int i = 0; i < 100_000; i++)
                interfacedCombined.lineIntersection(
                        Math.random(), Math.random(), Math.random(),
                        Math.random(), Math.random(), Math.random(),
                        intersection
                );

            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 100 mil interfaced rectangular prisms");
        }

        {
            long millis = System.currentTimeMillis();

            for (int i = 0; i < 100_000; i++)
                wrapperCombined.lineIntersection(
                        Math.random(), Math.random(), Math.random(),
                        Math.random(), Math.random(), Math.random(),
                        intersection
                );

            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 100 mil wrapped rectangular prisms");
        }
    }

    private static void benchmarkArea2d() {

        final Intersection<Vector2d> intersection = Intersection.ANY.cast();

        {
            long millis = System.currentTimeMillis();

            for (int i = 0; i < 1000; i++)
                polygonTests.lineIntersection(
                        Math.random(), Math.random(),
                        Math.random(), Math.random(),
                        intersection
                );

            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 100_000 2d polygons");
        }
    }

    private static void benchmarkCombinedCast() {
        final CombinedCast combinedCast = CombinedCast.builder()
                .gridSize(1.0)
                .max(100.0)
                .ordered(true)
                .build();

        {
            Vector3d vecA = Vector3d.of(Math.random(), Math.random(), Math.random());
            Vector3d vecB = Vector3d.of(Math.random(), Math.random(), Math.random());

            combinedCast.apply(combinedCastAreas, vecA, vecB);
        }

        {
            long millis = System.currentTimeMillis();

            int amount = 1000;

            for (int i = 0; i < amount; i++) {

                Vector3d vecA = Vector3d.of(Math.random(), Math.random(), Math.random());
                Vector3d vecB = Vector3d.of(Math.random(), Math.random(), Math.random());

                combinedCast.apply(combinedCastAreas, vecA, vecB);
            }


            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to do " + amount + " combined casts with 100 entities and 100 block range");
        }
    }
}
