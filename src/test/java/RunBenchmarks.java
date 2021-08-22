import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.grid.GridCast;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.Iterator;

public class RunBenchmarks {

    private static Area3d combined;

    public static void main(String[] args) {


        Area3d.CONVERTER.register(Entity.class, entity ->
            new Area3d() {
                @Override
                public double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {
                    return new double[0];
                }
            }
        );

        long startMillis = System.currentTimeMillis();
        System.out.println("Setting up Area3ds");

        Area3d[] area3ds = new Area3d[1000];

        Arrays.fill(area3ds, new Area3dRectangularPrism() {
            @Override
            public double getMinX() {
                return Math.random();
            }
            @Override
            public double getMinY() {
                return Math.random();
            }
            @Override
            public double getMinZ() {
                return Math.random();
            }
            @Override
            public double getMaxX() {
                return Math.random();
            }
            @Override
            public double getMaxY() {
                return Math.random();
            }
            @Override
            public double getMaxZ() {
                return Math.random();
            }
        });

        combined = Area3d.combined(area3ds);

        System.out.println("Finished after " + (System.currentTimeMillis() - startMillis) + "ms");

        benchmarkBlocks();
        benchmarkArea3d();
    }

    private static void benchmarkBlocks() {
        long startMillis = System.currentTimeMillis();

        Iterator<double[]> iterator = GridCast.createGridIterator(
                Math.random(), Math.random(), Math.random(),
                Math.random(), Math.random(), Math.random(),
                1.0,
                1_000_000
        );

        while (iterator.hasNext()) {
            iterator.next();
        }

        System.out.println("took " + (System.currentTimeMillis() - startMillis) + "ms to iterate over 1 mil blocks");
    }

    private static void benchmarkArea3d() {
        long millis = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++)
            combined.lineIntersection(
                    Math.random(), Math.random(), Math.random(),
                    Math.random(), Math.random(), Math.random()
            );

        System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 1 mil rectangular prisms");
    }
}
