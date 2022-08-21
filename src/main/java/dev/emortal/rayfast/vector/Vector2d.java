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
    default int dimensions() {
        return 2;
    }

    @Override
    default double get(int index) {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            default -> throw new IndexOutOfBoundsException();
        };
    }

    @Override
    default Vector2d with(double... values) {
        if (values.length != 2) {
            throw new IllegalArgumentException("Vector2d requires 2 values");
        }
        return Vector2d.of(values[0], values[1]);
    }

    static Vector2d of(double x, double y) {
        return new Vector2dImpl(x, y);
    }
}
