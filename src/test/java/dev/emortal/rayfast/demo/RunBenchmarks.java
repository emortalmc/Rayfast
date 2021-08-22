package dev.emortal.rayfast.demo;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.grid.GridCast;

import java.util.Arrays;
import java.util.Iterator;

public class RunBenchmarks {

    private static Area3d interfacedCombined;
    private static Area3d wrapperCombined;

    public static void main(String[] args) {

        // Register entity to rayfast converter
        Area3d.CONVERTER.register(Entity.class, Entity::getBoundingBox);

        long startMillis = System.currentTimeMillis();
        System.out.println("Setting up Area3ds");

        Area3d[] interfaced = new Area3d[1000];

        // Create new entity
        Entity entity = new Entity() {
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

        Arrays.fill(interfaced, Area3d.CONVERTER.from(entity));

        interfacedCombined = Area3d.combined(interfaced);


        Area3d[] wrappers = new Area3d[1000];

        Arrays.fill(wrappers, Area3dRectangularPrism.wrapper(
                entity.getBoundingBox(),
                Entity.BoundingBox::getMinX,
                Entity.BoundingBox::getMinY,
                Entity.BoundingBox::getMinZ,
                Entity.BoundingBox::getMaxX,
                Entity.BoundingBox::getMaxY,
                Entity.BoundingBox::getMaxZ
        ));

        wrapperCombined = Area3d.combined(wrappers);


        System.out.println("Finished after " + (System.currentTimeMillis() - startMillis) + "ms");

        benchmarkBlocks();
        benchmarkArea3d();
    }

    private static void benchmarkBlocks() {
        long startMillis = System.currentTimeMillis();

        Iterator<double[]> iterator = GridCast.createGridIterator(
                Math.random(), Math.random(), Math.random(),
                Math.random(), Math.random(), Math.random(),
                1,
                100_000
        );

        int i = 0;

        while (iterator.hasNext()) {
            iterator.next();
            i++;
        }

        System.out.println("took " + (System.currentTimeMillis() - startMillis) + "ms to iterate over " + i + " grid units (1x1 cubes)");
    }

    private static void benchmarkArea3d() {
        {
            long millis = System.currentTimeMillis();

            for (int i = 0; i < 100_000; i++)
                interfacedCombined.lineIntersection(
                        Math.random(), Math.random(), Math.random(),
                        Math.random(), Math.random(), Math.random()
                );

            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 100 mil interfaced rectangular prisms");
        }

        {
            long millis = System.currentTimeMillis();

            for (int i = 0; i < 100_000; i++)
                wrapperCombined.lineIntersection(
                        Math.random(), Math.random(), Math.random(),
                        Math.random(), Math.random(), Math.random()
                );

            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 100 mil wrapped rectangular prisms");
        }
    }
}
