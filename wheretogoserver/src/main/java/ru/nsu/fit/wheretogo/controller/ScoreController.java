package ru.nsu.fit.wheretogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.ScoreDTO;
import ru.nsu.fit.wheretogo.dto.UserDTO;
import ru.nsu.fit.wheretogo.service.ScoreService;

@RestController
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PutMapping()
    public ScoreDTO addScore(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "place_id") Long placeId,
            @RequestParam(name = "score") Integer value
    ) {
        return scoreService.createScore(userId, placeId, value);
    }

    @DeleteMapping("/{user_id}/{place_id}")
    public ResponseEntity<String> deleteUserScore(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "place_id") Long placeId) {
        scoreService.deleteScore(new ScoreDTO().setAuthor(userId).setPlace(placeId));
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<PagedListDTO<ScoreDTO>> getScores(
            @RequestParam(name = "placeId") Long placeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(scoreService.getByPlace(new PlaceDescriptionDTO().setId(placeId), page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<PagedListDTO<ScoreDTO>> getByUser(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(scoreService.getByUserId(new UserDTO().setId(userId), page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/{place_id}")
    public ResponseEntity<ScoreDTO> getUserPlaceSore(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "place_id") Long placeId) {
        ScoreDTO dto = scoreService.getByUserPlace(userId, placeId);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/exist_any/{user_id}")
    public ResponseEntity<Boolean> isUserHasScores(
            @PathVariable(name = "user_id") Long userId
    ) {
        return new ResponseEntity<>(scoreService.ifUserHasScores(userId), HttpStatus.OK);
    }
}
