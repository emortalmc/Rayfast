package dev.emortal.rayfast.test.unit;

import dev.emortal.rayfast.area.Intersection;
import dev.emortal.rayfast.area.area2d.Area2d;
import dev.emortal.rayfast.area.area2d.Area2dRectangle;
import dev.emortal.rayfast.util.Intersection2dUtils;
import dev.emortal.rayfast.vector.Vector2d;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

class Intersection2dUtilsTest {
    @Test
    public void testIntersection() {
        assertIntersection(Vector2d.of(0, 0), Vector2d.of(1, 0),
                           Vector2d.of(3, -1), Vector2d.of(3, 1),
                           Vector2d.of(3, 0));
        assertIntersection(Vector2d.of(0, 0), Vector2d.of(-1, 0),
                            Vector2d.of(-3, -1), Vector2d.of(-3, 1),
                            Vector2d.of(-3, 0));
        assertIntersection(Vector2d.of(0, 0), Vector2d.of(0, 1),
                            Vector2d.of(-1, 3), Vector2d.of(1, 3),
                            Vector2d.of(0, 3));
        assertIntersection(Vector2d.of(0, 0), Vector2d.of(0, -1),
                            Vector2d.of(-1, -3), Vector2d.of(1, -3),
                            Vector2d.of(0, -3));
    }

    private void assertIntersection(Vector2d lineAStart, Vector2d lineADir, Vector2d lineBStart, Vector2d lineBDir, Vector2d result) {
        Area2d area = Area2d.combined();
        var intersectionResult = Intersection2dUtils.lineIntersection(area, Intersection.Direction.ANY,
                lineAStart.x(), lineAStart.y(),
                lineADir.x(), lineADir.y(),
                lineBStart.x(), lineBStart.y(),
                lineBDir.x(), lineBDir.y());

        Vector2d intersection = intersectionResult.intersection();

        assertEquals(result.x(), intersection.x(), 0.00001);
        assertEquals(result.y(), intersection.y(), 0.00001);
    }
}