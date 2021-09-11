package dev.emortal.rayfast.demo;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.area.area2d.Area2dPolygon;
import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.grid.GridCast;
import dev.emortal.rayfast.vector.Vector2d;

import java.util.*;

public class RunBenchmarks {

    private static Area3d interfacedCombined;
    private static Area3d wrapperCombined;
    private static Area2d polygonTests;

    public static void main(String[] args) {
        // Register entity to rayfast converter
        Area3d.CONVERTER.register(ExampleRaycastEntity.class, ExampleRaycastEntity::getBoundingBox);

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

        benchmarkBlocks();
        benchmarkArea3d();
        benchmarkArea2d();
    }

    private static void benchmarkBlocks() {
        long startMillis = System.currentTimeMillis();

        Iterable<double[]> iterable = GridCast.createGridIterator(
                Math.random(), Math.random(), Math.random(),
                Math.random(), Math.random(), Math.random(),
                1,
                100_000
        );

        int i = 0;

        for (double[] ignored : iterable) {
            i++;
        }

        System.out.println("took " + (System.currentTimeMillis() - startMillis) + "ms to iterate over " + i + " grid units (1x1 cubes)");
    }

    private static void benchmarkArea3d() {

        final Intersection<double[]> intersection = Intersection.ANY;

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

        final Intersection<double[]> intersection = Intersection.ANY;

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
}
