
package hu.nje.javaeloadas.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BmiControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        BmiCalculator calculator = new BmiCalculator();
        BmiController controller = new BmiController(calculator);

        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void getBmiPageReturnsForm() throws Exception {
        mockMvc.perform(get("/bmi"))
                .andExpect(status().isOk())
                .andExpect(view().name("bmi"))
                .andExpect(model().attributeExists("bmiForm"));
    }

    @Test
    void postValidDataReturnsResult() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "75")
                        .param("heightCm", "180"))
                .andExpect(status().isOk())
                .andExpect(view().name("bmi"))
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attribute("category", "Normál testsúly"));
    }

    @Test
    void postUnderweightReturnsCorrectCategory() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "50")
                        .param("heightCm", "180"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("category", "Sovány"));
    }

    @Test
    void postOverweightReturnsCorrectCategory() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "85")
                        .param("heightCm", "175"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("category", "Túlsúly"));
    }

    @Test
    void postObesityReturnsCorrectCategory() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "110")
                        .param("heightCm", "175"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("category", "Elhízás"));
    }

    @Test
    void postZeroWeightReturnsError() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "0")
                        .param("heightCm", "180"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeDoesNotExist("result"));
    }

    @Test
    void postNegativeHeightReturnsError() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "75")
                        .param("heightCm", "-180"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void postMissingWeightReturnsError() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("heightCm", "180"))
                .andExpect(status().isOk())
                .andExpect(model().attribute(
                        "error",
                        "Mindkét mező kitöltése kötelező!"
                ));
    }

    @Test
    void postMissingHeightReturnsError() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "75"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void postValidDataHasNoError() throws Exception {
        mockMvc.perform(post("/bmi")
                        .param("weight", "75")
                        .param("heightCm", "180"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"));
    }
}
