package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.util.Converter;

import java.util.function.Function;

public interface Vector3d extends Vector<Vector3d> {
    Converter<Vector3d> CONVERTER = new Converter<>();

    double x();
    double y();
    double z();

    @Override
    default int getDimensions() {
        return 3;
    }

    @Override
    default double get(int index) {
        switch (index) {
            case 0:
                return x();
            case 1:
                return y();
            case 2:
                return z();
            default:
                throw new IllegalArgumentException("Index must be between 0 and 2");
        }
    }

    @Override
    default Function<double[], Vector3d> getFactory() {
        return (double[] values) -> {
            if (values.length != 3) {
                throw new IllegalArgumentException("Expected 2 values, got " + values.length);
            }
            return of(values[0], values[1], values[2]);
        };
    }

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
