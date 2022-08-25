package dev.emortal.rayfast.test.examples;

import dev.emortal.rayfast.area.area3d.Area3d;
import dev.emortal.rayfast.area.area3d.Area3dRectangularPrism;
import dev.emortal.rayfast.vector.Vector3d;

public class WikiExamples {
    public static void main(String[] args) {
// Create a rectangular prism with random coordinates
Area3d rectangularPrism = Area3dRectangularPrism.of(
        Math.random(), Math.random(), Math.random(), // Min position
        Math.random(), Math.random(), Math.random() // Max position
);

// Cast a ray though it, and check if it was intersected
Vector3d intersection = rectangularPrism.lineIntersection(
        Math.random(), Math.random(), Math.random(), // Line point
        Math.random(), Math.random(), Math.random() // Line direction
);

System.out.println("Line Intersected: " + (intersection != null));

    }
}
