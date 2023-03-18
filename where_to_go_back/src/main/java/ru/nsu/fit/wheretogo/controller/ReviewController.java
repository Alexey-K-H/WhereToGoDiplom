package ru.nsu.fit.wheretogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.ReviewDTO;
import ru.nsu.fit.wheretogo.dto.UserDTO;
import ru.nsu.fit.wheretogo.service.ReviewService;

import java.time.Instant;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping()
    public ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO) {
        service.addReview(reviewDTO.setWrittenAt(Instant.now()));
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable(name = "id") Long id) {
        service.deleteReview(new ReviewDTO().setId(id));
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<PagedListDTO<ReviewDTO>> getReviews(
            @RequestParam(name = "placeId") Long placeId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(service.getByPlace(new PlaceDescriptionDTO().setId(placeId), page, pageSize), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<PagedListDTO<ReviewDTO>> getByUser(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ) {
        return new ResponseEntity<>(service.getByUser(new UserDTO().setId(userId), page, pageSize), HttpStatus.OK);
    }

}
