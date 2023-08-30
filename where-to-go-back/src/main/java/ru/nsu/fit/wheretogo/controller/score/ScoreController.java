package ru.nsu.fit.wheretogo.controller.score;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.user.ScoreDTO;
import ru.nsu.fit.wheretogo.dto.user.UserDTO;
import ru.nsu.fit.wheretogo.service.score.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

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

        var dto = new ScoreDTO();
        dto.setAuthor(userId);
        dto.setPlace(placeId);

        scoreService.deleteScore(dto);
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ScoreDTO>> getScores(
            @RequestParam(name = "placeId") Long placeId
    ) {
        var dto = new PlaceDescriptionDTO();
        dto.setId(placeId);

        return new ResponseEntity<>(scoreService.getByPlace(dto), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ScoreDTO>> getByUser(
            @RequestParam(name = "userId") Long userId
    ) {
        var dto = new UserDTO();
        dto.setId(userId);

        return new ResponseEntity<>(scoreService.getByUser(dto), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/{place_id}")
    public ResponseEntity<ScoreDTO> getUserPlaceSore(
            @PathVariable(name = "user_id") Long userId,
            @PathVariable(name = "place_id") Long placeId) {
        ScoreDTO dto = scoreService.getByPlaceScoreFromUser(userId, placeId);
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
