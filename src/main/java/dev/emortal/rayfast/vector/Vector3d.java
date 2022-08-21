package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.util.Converter;

import java.util.function.Function;

public interface Vector3d extends Vector<Vector3d> {
    Converter<Vector3d> CONVERTER = new Converter<>();

    double x();
    double y();
    double z();

    @Override
    default int dimensions() {
        return 3;
    }

    @Override
    default double get(int index) {
        return switch (index) {
            case 0 -> x();
            case 1 -> y();
            case 2 -> z();
            default -> throw new IllegalArgumentException("Index must be between 0 and 2");
        };
    }

    @Override
    default Vector3d with(double... values) {
        if (values.length != 3) {
            throw new IllegalArgumentException("Vector3d requires 3 values");
        }
        return Vector3d.of(values[0], values[1], values[2]);
    }

    static Vector3d of(double x, double y, double z) {
        return new Vector3dImpl(x, y, z);
    }
}
