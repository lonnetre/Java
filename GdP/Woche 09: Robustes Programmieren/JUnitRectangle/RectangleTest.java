package JUnitRectangle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class RectangleTest_L {

    @Test
    @DisplayName("Should compute perimeter with positive side lengths")  // A readable name for the test's output
    void shouldComputePerimeterWithPositiveSideLengths() {
        Assertions.assertEquals(18, Rectangle_L.computePerimeter(5, 4));
    }

    @Test
    @DisplayName("Should compute perimeter with a width of zero")
    void shouldComputePerimeterWithAWidthOfZero() {
        Assertions.assertEquals(246, Rectangle_L.computePerimeter(0, 123));
    }

    @Test
    @DisplayName("Should compute perimeter with a height of zero")
    void shouldComputePerimeterWithAHeightOfZero() {
        Assertions.assertEquals(10, Rectangle_L.computePerimeter(5, 0));
    }

    @Test
    @DisplayName("Should compute perimeter with side lengths of zero")
    void shouldComputePerimeterWithSideLengthsOfZero() {
        Assertions.assertEquals(0, Rectangle_L.computePerimeter(0, 0));
    }

    @Test
    @DisplayName("Should not compute perimeter with negative width")
    void shouldNotComputePerimeterWithNegativeWidth() {
        Assertions.assertThrows(ArithmeticException.class, () -> {
            Rectangle_L.computePerimeter(-67, 5);
        });
    }

    @Test
    @DisplayName("Should not compute perimeter with negative height")
    void shouldNotComputePerimeterWithNegativeHeight() {
        Assertions.assertThrows(ArithmeticException.class, () -> {
            Rectangle_L.computePerimeter(7, -24);
        });
    }

    @Test
    @DisplayName("Should not compute perimeter with negative side lengths")
    void shouldNotComputePerimeterWithNegativeSideLengths() {
        Assertions.assertThrows(ArithmeticException.class, () -> {
            Rectangle_L.computePerimeter(-67, -45);
        });
    }
}
