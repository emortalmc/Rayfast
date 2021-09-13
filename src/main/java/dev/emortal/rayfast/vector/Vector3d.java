package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.util.Converter;

public interface Vector3d extends Vector {
    Converter<Vector3d> CONVERTER = new Converter<>();

    double x();
    double y();
    double z();

    static Vector3d of(double x, double y, double z) {
        return new Vector3d() {
            @Override
            public double x() {
                return x;
            }

            @Override
            public double y() {
                return y;
            }

            @Override
            public double z() {
                return z;
            }

            @Override
            public String toString() {
                return "Vector3d(" + x + ", " + y + ", " + z + ")";
            }
        };
    }
}
