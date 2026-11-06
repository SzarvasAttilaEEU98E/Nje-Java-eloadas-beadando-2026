
package hu.nje.javaeloadas.react.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    @GetMapping({"/react", "/react/"})
    public String reactPage() {
        return "forward:/react/index.html";
    }
}
