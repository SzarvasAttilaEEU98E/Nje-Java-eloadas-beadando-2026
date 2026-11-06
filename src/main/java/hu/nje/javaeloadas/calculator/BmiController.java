
package hu.nje.javaeloadas.calculator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
public class BmiController {

    private final BmiCalculator bmiCalculator;

    public BmiController(BmiCalculator bmiCalculator) {
        this.bmiCalculator = bmiCalculator;
    }

    @GetMapping("/bmi")
    public String showForm(Model model) {
        model.addAttribute("bmiForm", new BmiForm());
        return "bmi";
    }

    @PostMapping("/bmi")
    public String calculate(
            @ModelAttribute("bmiForm") BmiForm bmiForm,
            Model model) {

        try {
            if (bmiForm.getWeight() == null ||
                    bmiForm.getHeightCm() == null) {
                throw new IllegalArgumentException(
                        "Mindkét mező kitöltése kötelező!"
                );
            }

            double bmi = bmiCalculator.calculate(
                    bmiForm.getWeight(),
                    bmiForm.getHeightCm()
            );

            String category = bmiCalculator.getCategory(bmi);

            model.addAttribute(
                    "result",
                    String.format(Locale.forLanguageTag("hu-HU"),
                            "%.2f", bmi)
            );

            model.addAttribute("category", category);

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "bmi";
    }
}
