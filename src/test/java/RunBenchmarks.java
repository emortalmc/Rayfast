import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.grid.GridCast;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RunBenchmarks {

    public static void main(String[] args) {
        {
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

            System.out.println("took " + (System.currentTimeMillis() - startMillis) + "ms to do 1 mil blocks");
        }

        long startMillis = System.currentTimeMillis();
        System.out.println("Setting up Area3ds");

        Set<Area3d> area3dSet = new LinkedHashSet<>();

        for (int i = 0; i < 1_000_000; i++) {
            area3dSet.add(new Area3dRectangularPrism(
                    Math.random(), Math.random(), Math.random(),
                    Math.random(), Math.random(), Math.random()
            ));
        }

        Area3d combined = Area3d.combined(area3dSet.toArray(Area3d[]::new));

        System.out.println("Finished after " + (System.currentTimeMillis() - startMillis) + "ms");

        {
            long millis = System.currentTimeMillis();

            for (int i = 0; i < 500; i++)
            combined.lineIntersection(
                    Math.random(), Math.random(), Math.random(),
                    Math.random(), Math.random(), Math.random()
            );

            System.out.println("took " + (System.currentTimeMillis() - millis) + "ms to intersect 1 mil rectangular prisms");
        }
    }
}
