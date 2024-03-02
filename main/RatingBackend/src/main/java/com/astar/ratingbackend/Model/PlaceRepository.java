/**
 * Project Name: Eagle_Rating
 * File Name:    PlaceRepository.java
 * Package Name: com.astar.ratingbackend.Repository
 *
 * Type: Repository
 * Purpose: Repository interface for Place Entity - Facilitates operations such as create, read, update, and delete (CRUD) for places in the system.
 *
 * Created on: [2024-02-21]
 * Author: @Wenzhuo Ma
 *
 * History:
 * - [2024-02-21] Created by @Wenzhuo Ma
 * - [Date] [Modification] [Modifier]
 * ...
 */

package com.astar.ratingbackend.Model;

import com.astar.ratingbackend.Entity.Place;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends MongoRepository<Place, ObjectId> {
    // Example custom query method
    List<Place> findByCategory(String category);
    @Query("{ '_id' : ?0, 'isDeleted' : false }")
    Optional<Place> findByIdAndNotDeleted(ObjectId id);

    Optional<Place> findById(ObjectId id);

    // Additional custom methods to facilitate specific queries, for example:
    // - Find places by tags
    // - Find places within a certain location
    // - Find places on a specific campus
    List<Place> findByLocNameContainingIgnoreCase(String locName);

    // Method to find places with names that contain the given string (case insensitive) and have a specific category
    List<Place> findByLocNameContainingIgnoreCaseAndCategory(String locName, String category);
    @Query("{ 'tags': { $all: ?0 } }")
    List<Place> findByTagsContainingAll(List<String> tags);
    @Query("{ 'locName': {$regex: ?0, $options: 'i'}, 'category': {$regex: ?1, $options: 'i'}, 'tags': {$all: ?2} }")
    List<Place> findByLocNameAndCategoryAndTagsAll(String locName, String category, List<String> tags);

}

