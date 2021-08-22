import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.grid.GridCast;

import java.util.Arrays;
import java.util.Iterator;

public class RunBenchmarks {

    private static Area3d combined;

    public static void main(String[] args) {
        long startMillis = System.currentTimeMillis();
        System.out.println("Setting up Area3ds");

        Area3d[] area3ds = new Area3d[1000];

        Arrays.fill(area3ds, new Area3dRectangularPrism(
                Math.random(), Math.random(), Math.random(),
                Math.random(), Math.random(), Math.random()
        ));

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

        for (int i = 0; i < 100_000; i++)
            combined.lineIntersection(
                    Math.random(), Math.random(), Math.random(),
                    Math.random(), Math.random(), Math.random()
            );

        System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 100 mil rectangular prisms");
    }
}
