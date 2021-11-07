package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.util.Converter;

import java.util.function.Function;

/**
 * An immutable 2d vector
 */
public interface Vector2d extends Vector<Vector2d> {
    Converter<Vector2d> CONVERTER = new Converter<>();

    double x();
    double y();

    @Override
    default int getDimensions() {
        return 2;
    }

    @Override
    default double get(int index) {
        switch (index) {
            case 0: return x();
            case 1: return y();
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    default Function<double[], Vector2d> getFactory() {
        return (double[] values) -> {
            if (values.length != 2) {
                throw new IllegalArgumentException("Expected 2 values, got " + values.length);
            }
            return of(values[0], values[1]);
        };
    }

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
