package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.util.Converter;

public interface Vector2d extends Vector {
    Converter<Vector2d> CONVERTER = new Converter<>();

    double x();
    double y();

    static Vector2d of(double x, double y) {
        return new Vector2d() {
            @Override
            public double x() {
                return x;
            }

            @Override
            public double y() {
                return y;
            }

            @Override
            public String toString() {
                return "Vector2d(" + x() + ", " + y() + ")";
            }
        };
    }
}
