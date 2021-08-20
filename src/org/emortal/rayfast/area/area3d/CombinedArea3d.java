package org.emortal.rayfast.area.area3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinedArea3d implements Area3d {

    private final double[][] planes;

    public CombinedArea3d(Area3d... area3ds) {
        List<double[]> planesList = new ArrayList<>();

        for (Area3d area3d : area3ds) {
            planesList.addAll(Arrays.asList(area3d.getPlanes()));
        }

        this.planes = planesList.toArray(double[][]::new);
    }

    @Override
    public double[][] getPlanes() {
        return planes;
    }
}
