
package hu.nje.javaeloadas.calculator;

import org.springframework.stereotype.Component;

@Component
public class BmiCalculator {

    public double calculate(double weight, double heightCm) {
        if (!Double.isFinite(weight) || !Double.isFinite(heightCm)
                || weight <= 0 || heightCm <= 0) {
            throw new IllegalArgumentException(
                    "A testsúlynak és a magasságnak pozitív számnak kell lennie!"
            );
        }

        double heightM = heightCm / 100.0;
        return weight / (heightM * heightM);
    }

    public String getCategory(double bmi) {
        if (!Double.isFinite(bmi) || bmi <= 0) {
            throw new IllegalArgumentException("Érvénytelen BMI-érték!");
        }

        if (bmi < 18.5) {
            return "Sovány";
        } else if (bmi < 25) {
            return "Normál testsúly";
        } else if (bmi < 30) {
            return "Túlsúly";
        } else {
            return "Elhízás";
        }
    }
}
