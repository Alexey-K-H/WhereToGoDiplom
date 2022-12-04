package ru.nsu.fit.wheretogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.StayPointDTO;
import ru.nsu.fit.wheretogo.service.StayPointService;

import java.util.List;

@RestController
@RequestMapping("/stay_point")
public class StayPointController {

    @Autowired
    private StayPointService stayPointService;

    @PutMapping
    public ResponseEntity<String> addStoryPoint(
            @RequestParam(name = "lat") Double lat,
            @RequestParam(name = "lon") Double lon
    ) {
        try {
            stayPointService.addStoryPoint(lat, lon);
            return ResponseEntity.ok("Added");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUserStoryPoint() {
        stayPointService.deleteOldStoryPoint();
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user")
    public ResponseEntity<List<StayPointDTO>> getByUser() {
        return new ResponseEntity<>(stayPointService.getByUser(), HttpStatus.OK);
    }

    @GetMapping("/exist_any")
    public ResponseEntity<Boolean> isUserHasStayPoints() {
        return new ResponseEntity<>(stayPointService.ifUserHasStayPoints(), HttpStatus.OK);
    }
}
