package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.util.Converter;
import org.jetbrains.annotations.NotNull;

public interface Vector2d extends Vector {
    Converter<Vector2d> CONVERTER = new Converter<>();

    double x();
    double y();

    /**
     * Generates a vector from the specified array
     * @param vector the array to generate a vector from
     * @return the generated vector
     */
    default @NotNull Vector2d from(double[] vector) {
        return of(vector[0], vector[1]);
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
