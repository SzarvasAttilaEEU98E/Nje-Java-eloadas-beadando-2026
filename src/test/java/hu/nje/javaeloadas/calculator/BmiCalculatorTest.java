package calculator;

import hu.nje.javaeloadas.calculator.BmiCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BmiCalculatorTest {

    private BmiCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new BmiCalculator();
    }

    @Test
    void normalBmiCalculation() {
        double result = calculator.calculate(75, 180);

        assertEquals(23.15, result, 0.01);
    }

    @Test
    void underweightCategory() {
        assertEquals("Sovány", calculator.getCategory(17.5));
    }

    @Test
    void normalWeightCategory() {
        assertEquals("Normál testsúly", calculator.getCategory(23.0));
    }

    @Test
    void overweightCategory() {
        assertEquals("Túlsúly", calculator.getCategory(27.0));
    }

    @Test
    void obesityCategory() {
        assertEquals("Elhízás", calculator.getCategory(32.0));
    }

    @Test
    void zeroWeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(0, 180));
    }

    @Test
    void negativeHeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(75, -180));
    }

    @Test
    void zeroHeightThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(75, 0));
    }

    @Test
    void categoryBoundaryValues() {
        assertEquals("Normál testsúly", calculator.getCategory(18.5));
        assertEquals("Túlsúly", calculator.getCategory(25.0));
        assertEquals("Elhízás", calculator.getCategory(30.0));
    }

    @Test
    void invalidBmiThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.getCategory(-1));
    }
}
