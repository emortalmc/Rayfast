package dev.emortal.rayfast.area.area3d;

import java.util.*;

public class Area3dCombined implements Area3d {

    protected double[][] planes;
    private Area3d[] all;
    private Area3d[] updatable;

    public Area3dCombined(Area3d... area3ds) {
        this.all = area3ds;
        generatePlanes();
        generateUpdatable();
    }

    public Area3dCombined(Collection<Area3d> area3ds) {
        this(area3ds.toArray(Area3d[]::new));
    }

    /**
     * Adds an Area3d to this CombinedArea3d.
     * <br>
     * This is an expensive operation
     * @param area3d the Area3d to add
     */
    public void add(Area3d area3d) {
        List<Area3d> list = Arrays.asList(all);
        list.add(area3d);
        all = list.toArray(Area3d[]::new);
        generatePlanes();
        generateUpdatable();
    }

    /**
     * Removes an Area3d from this CombinedArea3d.
     * <br>
     * This is an expensive operation
     * @param area3d the Area3d to remove
     */
    public void remove(Area3d area3d) {
        List<Area3d> list = Arrays.asList(all);
        list.remove(area3d);
        all = list.toArray(Area3d[]::new);
        generatePlanes();
        generateUpdatable();
    }

    /**
     * Generation of planes array
     */
    protected void generatePlanes() {
        List<double[]> planesList = new ArrayList<>();

        for (Area3d area3d : all) {
            planesList.addAll(Arrays.asList(area3d.getPlanes()));
        }

        this.planes = planesList.toArray(double[][]::new);
    }

    /**
     * Generation of planes array
     */
    protected void generateUpdatable() {
        Set<Area3d> updatableSet = new HashSet<>();

        for (Area3d area3d : all) {
            if (area3d.isUpdatable()) {
                updatableSet.add(area3d);
            }
        }

        this.updatable = updatableSet.toArray(Area3d[]::new);
    }

    @Override
    public double[][] getPlanes() {
        return planes;
    }

    @Override
    public void updatePlanes() {
        for (Area3d area3d : all) {
            area3d.updatePlanes();
        }
    }

    @Override
    public boolean isUpdatable() {
        return updatable.length > 0;
    }
}
