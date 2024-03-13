package com.astar.ratingbackend.Controller;

import com.astar.ratingbackend.Entity.CommentFilterRequest;
import com.astar.ratingbackend.Entity.Place;
import com.astar.ratingbackend.Entity.Rating;
import com.astar.ratingbackend.Entity.User;
import com.astar.ratingbackend.Service.IPlaceService;
import com.astar.ratingbackend.Service.IRatingService;
import com.astar.ratingbackend.Service.IUserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing rating entities.
 * Provides endpoints for CRUD operations and filtering on ratings.
 */
@RestController
@RequestMapping("/api/rating")
public class RatingController {
    @Autowired
    private IRatingService ratingService;
    @Autowired
    private IPlaceService placeService;
    @Autowired
    private IUserService userService;

    /**
     * Retrieves all ratings from the database.
     * @return A list of all ratings.
     */
    @GetMapping("/getAll")
    public List<Rating> getAllRating(@RequestParam(required = false) Boolean desc){
        if(desc!=null&&desc){
            return ratingService.getAllRatingsDesc();
        }
        return ratingService.getAllRatings();
    }

    /**
     * Retrieves a specific rating by its ID.
     * @param ratingId The ID of the rating to retrieve.
     * @return A ResponseEntity containing the found rating or a not found status.
     */
    @GetMapping("/get")
    public ResponseEntity<Rating> getRatingById(@RequestParam String ratingId){
        try {
            ObjectId objectId = new ObjectId(ratingId);
            Optional<Rating> rating = ratingService.getRateById(objectId);
            return rating.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Invalid ObjectId format
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Other errors
        }
    }

    /**
     * Deletes a specific rating by its ID.
     * @param ratingId The ID of the rating to delete.
     * @return A ResponseEntity indicating the outcome of the operation.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteRatingById(@RequestParam String ratingId) {
        try {
            Rating rating=ratingService.validateRating(ratingId);
            User user = userService.validateUser(rating.getUserId());
            Place place = placeService.validatePlace(rating.getPlaceId());
            boolean ratingDeleted = ratingService.deleteRating(ratingId);
            boolean userDeleted = userService.deleteRating(rating);
            boolean placeDeleted = placeService.deleteRating(rating);
            if (!ratingDeleted || !userDeleted || !placeDeleted) {
                // If any deletion fails, return an INTERNAL_SERVER_ERROR response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            // If deletion is successful, return an OK response
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // If an IllegalArgumentException occurs, return an INTERNAL_SERVER_ERROR response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Adds a new rating to the database.
     * @param rating The rating entity to be added.
     * @return A ResponseEntity containing the created rating or an error status.
     */
    @PostMapping("/addRating")
    public ResponseEntity<Place> addRating(@RequestBody Rating rating) {
        String placeId = rating.getPlaceId();
        try {
            User user = userService.validateUser(rating.getUserId());
            Place place = placeService.validatePlace(placeId);
            Rating addedRating = ratingService.addRating(rating, user);
            userService.addRating(addedRating);
            ResponseEntity<Place> response = placeService.addRating(placeId, addedRating);
            return ResponseEntity.ok(response.getBody());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves ratings that match specific filter criteria.
     * @param commentFilterRequest Contains the filter criteria for ratings.
     * @return A list of ratings that match the filter criteria.
     */
    @PostMapping("/filter")
    public List<Rating> getRatingByFilter(@RequestBody CommentFilterRequest commentFilterRequest) {
        Rating.OverallRating overallRating = commentFilterRequest.getOverallRating();
        int floor = commentFilterRequest.getFloor() != null ? commentFilterRequest.getFloor() : -1;
        return ratingService.getRatingByFilter(overallRating, floor);
    }
}
