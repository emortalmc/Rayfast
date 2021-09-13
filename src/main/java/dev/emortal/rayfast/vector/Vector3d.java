package dev.emortal.rayfast.vector;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.util.Converter;
import org.jetbrains.annotations.NotNull;

public interface Vector3d extends Vector {
    Converter<Vector3d> CONVERTER = new Converter<>();

    double x();
    double y();
    double z();

    /**
     * Generates a vector from the specified array
     * @param vector the array to generate a vector from
     * @return the generated vector
     */
    static @NotNull Vector3d from(double[] vector) {
        return of(vector[0], vector[1], vector[2]);
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
