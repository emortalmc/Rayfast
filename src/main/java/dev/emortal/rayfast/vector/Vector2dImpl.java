package dev.emortal.rayfast.vector;

record Vector2dImpl(double x, double y) implements Vector2d {

    @Override
    public String toString() {
        return "Vector2d(" + x + ", " + y + ")";
    }
}
