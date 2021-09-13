package dev.emortal.rayfast.casting.combined;

import dev.emortal.rayfast.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CombinedCastResult {

    class HitResult {

        private final Object subject;
        private final HitType type;
        private final Vector3d position;
        private final double distanceSquared;

        public HitResult(
                @Nullable Object subject,
                @NotNull HitType type,
                @NotNull Vector3d position,
                double distanceSquared
        ) {
            this.subject = subject;
            this.type = type;
            this.position = position;
            this.distanceSquared = distanceSquared;
        }

        @SuppressWarnings("unchecked")
        public <R> @NotNull R subject() {
            if (subject == null)
                throw new IllegalStateException("Subject was null");
            return (R) subject;
        }

        public @NotNull HitType type() {
            return type;
        }

        public @NotNull Vector3d position() {
            return position;
        }

        public double distanceSquared() {
            return distanceSquared;
        }
    }

    enum HitType {
        GRIDUNIT,
        AREA3D
    }
}
