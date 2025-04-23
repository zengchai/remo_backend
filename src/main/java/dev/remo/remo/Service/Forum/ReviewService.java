package dev.remo.remo.Service.Forum;

import java.util.List;

import dev.remo.remo.Models.Review.Review;

public interface ReviewService {
    public List<Review> getReviewbyIds(List<String> reviewIds);
}
