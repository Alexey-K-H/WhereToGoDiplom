package ru.nsu.fit.wheretogo.controller.ors;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.model.ors.ORSMatrixRequest;
import ru.nsu.fit.wheretogo.model.ors.ORSMatrixResponse;
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

    @PostMapping("/matrix")
    public ResponseEntity<ORSMatrixResponse> matrix(@RequestBody ORSMatrixRequest request) {
        return new ResponseEntity<>(openRouteService.getPlacesDurationMatrix(request), HttpStatus.OK);
    }
}
