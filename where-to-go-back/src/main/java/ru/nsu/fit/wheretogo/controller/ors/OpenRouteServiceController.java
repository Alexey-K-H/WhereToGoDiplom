package ru.nsu.fit.wheretogo.controller.ors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.route.ORSDirectionsDto;
import ru.nsu.fit.wheretogo.service.openroute.OpenRouteService;

@RestController
@RequestMapping("/ors")
@RequiredArgsConstructor
public class OpenRouteServiceController {

    private final OpenRouteService openRouteService;

    @GetMapping("/health-check")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>(openRouteService.health(), HttpStatus.OK);
    }

    @PostMapping("/route/driving")
    public ResponseEntity<ORSDirectionsDto> getRouteDriving() {
        return new ResponseEntity<>(openRouteService.getDirectionDriving(), HttpStatus.OK);
    }

    @PostMapping("/route/walking")
    public ResponseEntity<ORSDirectionsDto> getRouteWalking() {
        return new ResponseEntity<>(new ORSDirectionsDto(), HttpStatus.OK);
    }

}
