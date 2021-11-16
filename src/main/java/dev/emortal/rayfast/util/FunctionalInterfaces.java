package dev.emortal.rayfast.util;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.vector.Vector2d;
import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

@ApiStatus.Internal
public class FunctionalInterfaces {

    @FunctionalInterface
    public interface Vector3dToBoolean {
        boolean apply(@NotNull Vector3d vector3d);
    }

    @FunctionalInterface
    public interface Vector3dArea3dToBoolean {
        boolean apply(@NotNull Vector3d vector3d, @NotNull Area3d area3d);
    }

    @FunctionalInterface
    public interface DoubleWrapper<T> {
        double get(@NotNull T object);
    }

    @FunctionalInterface
    public interface Lines2dWrapper<T> extends Function<T, @NotNull Map<Vector2d, Vector2d>> {
    }

    @FunctionalInterface
    public interface Lines3dWrapper<T> extends Function<T, @NotNull Map<Vector3d, Vector3d>> {
    }

    @FunctionalInterface
    public interface Vector3dWrapper<T> extends Function<T, Vector3d> {
    }
}
