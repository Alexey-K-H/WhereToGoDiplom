package ru.nsu.fit.wheretogo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.dto.ReviewDto;
import ru.nsu.fit.wheretogo.dto.UserDto;
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
    public Review addReview(ReviewDto reviewDto) {
        return reviewRepository.save(Review.getFromDto(reviewDto));
    }

    @Transactional
    public void deleteReview(ReviewDto reviewDto) {
        reviewRepository.delete(Review.getFromDto(reviewDto));
    }

    public PagedListDTO<ReviewDto> getByUser(UserDto userDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByAuthor(User.getFromDTO(userDto), pageRequest);
        List<ReviewDto> userReviewDtos = reviews.toList()
                .stream()
                .map(ReviewDto::getFromEntity)
                .collect(toList());
        return new PagedListDTO<ReviewDto>()
                .setList(userReviewDtos)
                .setPageNum(page)
                .setTotalPages(reviews.getTotalPages());
    }

    public PagedListDTO<ReviewDto> getByPlace(PlaceDescriptionDTO placeDto, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByPlace(Place.getFromDTO(placeDto), pageRequest);
        List<ReviewDto> placeReviewDtos = reviews.toList()
                .stream()
                .map(ReviewDto::getFromEntity)
                .collect(toList());
        return new PagedListDTO<ReviewDto>()
                .setList(placeReviewDtos)
                .setPageNum(page)
                .setTotalPages(reviews.getTotalPages());
    }
}
