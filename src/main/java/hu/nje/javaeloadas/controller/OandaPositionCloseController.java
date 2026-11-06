
package hu.nje.javaeloadas.controller;

import hu.nje.javaeloadas.service.OandaPositionCloseService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class OandaPositionCloseController {

    private final OandaPositionCloseService oandaPositionCloseService;

    public OandaPositionCloseController(
            OandaPositionCloseService oandaPositionCloseService) {
        this.oandaPositionCloseService = oandaPositionCloseService;
    }

    @GetMapping({"/forex/close", "/forex/close/page"})
    public String closePage() {
        return "forex-positions";
    }

    @ResponseBody
    @PutMapping(
            value = "/forex/positions/close/long",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> lezarLongPozicio(
            @RequestParam String devizapar)
            throws IOException, InterruptedException {

        String jsonValasz =
                oandaPositionCloseService.lezarPozicio(
                        devizapar,
                        "long"
                );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonValasz);
    }

    @ResponseBody
    @PutMapping(
            value = "/forex/positions/close/short",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> lezarShortPozicio(
            @RequestParam String devizapar)
            throws IOException, InterruptedException {

        String jsonValasz =
                oandaPositionCloseService.lezarPozicio(
                        devizapar,
                        "short"
                );

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonValasz);
    }

    @ResponseBody
    @PutMapping(
            value = "/forex/trades/{tradeId}/close",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> lezarTrade(
            @PathVariable long tradeId)
            throws IOException, InterruptedException {

        if (tradeId <= 0) {
            throw new IllegalArgumentException(
                    "A tradeId csak pozitív egész szám lehet."
            );
        }

        String jsonValasz =
                oandaPositionCloseService.lezarTrade(tradeId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonValasz);
    }
}
