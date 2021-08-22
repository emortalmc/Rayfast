package dev.emortal.rayfast.area.area3d;

import java.util.*;

public class Area3dCombined implements Area3d {

    private Area3d[] all;

    public Area3dCombined(Area3d... area3ds) {
        this.all = area3ds;
    }

    public Area3dCombined(Collection<Area3d> area3ds) {
        this(area3ds.toArray(Area3d[]::new));
    }

    @Override
    public double[] lineIntersection(double posX, double posY, double posZ, double dirX, double dirY, double dirZ) {

        for (Area3d area3d : all) {
            double[] intersection = area3d.lineIntersection(posX, posY, posZ, dirX, dirY, dirZ);

            if (intersection != null) {
                return intersection;
            }
        }

        return null;
    }
}
