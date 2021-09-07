package dev.emortal.rayfast.util;

import dev.emortal.rayfast.area.area3d.Area3d;

public interface Vector3d {
    Converter<Area3d> CONVERTER = new Converter<>();

    double x();
    double y();
    double z();
}
