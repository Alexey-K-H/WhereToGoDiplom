package ru.nsu.fit.wheretogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.wheretogo.dto.*;
import ru.nsu.fit.wheretogo.service.ScoreService;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService service;

    @PostMapping()
    public ResponseEntity<String> addScore(@RequestBody ScoreDTO scoreDTO) {
        service.addScore(scoreDTO);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteScore(@PathVariable(name = "id") Long id) {
        service.deleteScore(new ScoreDTO().setId(id));
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<PagedListDTO<ScoreDTO>> getScores(
            @RequestParam(name = "placeId") Long placeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(service.getByPlace(new PlaceDescriptionDTO().setId(placeId), page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<PagedListDTO<ScoreDTO>> getByUser(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(service.getByUser(new UserDto().setId(userId), page, pageSize), HttpStatus.OK);
    }
}
