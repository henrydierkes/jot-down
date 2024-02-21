
/**
 * Project Name: Eagle_Rating
 * File Name:    Comment.java
 * Package Name: com.astar.ratingbackend.Entity
 *
 * Type: Entity
 * Purpose: Comment Entity - Represents a comment in the system, including details such as the comment ID, user ID, text, and date of the comment.
 *
 * Created on: [2024-02-21]
 * Author: @Wenzhuo Ma
 *
 * History:
 * - [2024-02-21] Created by @Wenzhuo Ma
 * - [Date] [Modification] [Modifier]
 * ...
 */

package com.astar.ratingbackend.Entity;

import lombok.Data;
import org.bson.types.ObjectId; // Ensure correct ObjectId import
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "comments") // Specify the collection name
public class Comment implements Serializable {

    @Id // This annotation is used to specify the primary key
    @Field("commentId")
    private ObjectId commentId; // Changed to commentId

    @Field("userId")
    private ObjectId userId; // Added for linking the comment to a user

    @Field("text")
    private String text; // The actual comment text

    @Field("date")
    private Date date; // The date when the comment was made

    // Constructors, getters, and setters are generated by Lombok through @Data
}
