package dev.emortal.rayfast.util;

import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@ApiStatus.Internal
public class Wrappers {

    @FunctionalInterface
    public interface DoubleWrapper<T> {
        double get(T object);
    }

    @FunctionalInterface
    public interface LinesWrapper<T> {
        Map<Vector2d, Vector2d> get(T object);
    }

    @FunctionalInterface
    public interface Vector3dWrapper<T> {
        Vector3d get(T object);
    }
}
