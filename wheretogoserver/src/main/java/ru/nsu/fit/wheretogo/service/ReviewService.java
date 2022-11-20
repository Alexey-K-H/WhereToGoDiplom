package ru.nsu.fit.wheretogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.ReviewDTO;
import ru.nsu.fit.wheretogo.dto.UserDTO;
import ru.nsu.fit.wheretogo.entity.Place;
import ru.nsu.fit.wheretogo.entity.Review;
import ru.nsu.fit.wheretogo.entity.User;
import ru.nsu.fit.wheretogo.repository.ReviewRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public Review addReview(ReviewDTO reviewDto) {
        return reviewRepository.save(Review.getFromDto(reviewDto));
    }

    @Transactional
    public void deleteReview(ReviewDTO reviewDto) {
        reviewRepository.delete(Review.getFromDto(reviewDto));
    }

    public PagedListDTO<ReviewDTO> getByUser(UserDTO userDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByAuthor(User.getFromDTO(userDto), pageRequest);
        List<ReviewDTO> userReviewDtos = reviews.toList()
                .stream()
                .map(ReviewDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<ReviewDTO>()
                .setList(userReviewDtos)
                .setPageNum(page)
                .setTotalPages(reviews.getTotalPages());
    }

    public PagedListDTO<ReviewDTO> getByPlace(PlaceDescriptionDTO placeDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByPlace(Place.getFromDTO(placeDto), pageRequest);
        List<ReviewDTO> placeReviewDtos = reviews.toList()
                .stream()
                .map(ReviewDTO::getFromEntity)
                .collect(toList());
        return new PagedListDTO<ReviewDTO>()
                .setList(placeReviewDtos)
                .setPageNum(page)
                .setTotalPages(reviews.getTotalPages());
    }
}
