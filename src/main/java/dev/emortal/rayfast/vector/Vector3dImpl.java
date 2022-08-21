package dev.emortal.rayfast.vector;

record Vector3dImpl(double x, double y, double z) implements Vector3d {
    @Override
    public String toString() {
        return "Vector3d(" + x + ", " + y + ", " + z + ")";
    }
}
