package com.shop.backend.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.backend.dtos.CommentRequest;
import com.shop.backend.dtos.ContactRequest;
import com.shop.backend.dtos.LikeRequest;
import com.shop.backend.dtos.OrderPaymentInfoDto;
import com.shop.backend.entities.*;
import com.shop.backend.repositories.CommentLikesRepository;
import com.shop.backend.repositories.CommentsRepository;
import com.shop.backend.repositories.ProductRepository;
import com.shop.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.Optional;
@RestController
public class CommentsController {
    @Autowired
    private CommentsRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CommentLikesRepository commentLikesRepository;
    @GetMapping("/comments/all")
    public List<Comments> getAllOrders() {
        return commentRepository.findAll();
    }
    @PostMapping("/createComment")
    public ResponseEntity<Comments> createComment(@RequestBody CommentRequest commentRequest) {
        try {
            Comments comments = new Comments();
            User user = userRepository.findById(commentRequest.getIdu()).orElse(null);
            Product product = productRepository.findById(commentRequest.getIdp()).orElse(null);

            comments.setUser(user);
            comments.setProduct(product);
            comments.setText(commentRequest.getText());
            comments.setLikes(commentRequest.getLikes());
            comments.setDeleted(commentRequest.isDeleted());
            comments.setActive(commentRequest.isActive());
            comments.setData(commentRequest.getData());
            comments.setRating(commentRequest.getRating());
            Comments createdComment = commentRepository.save(comments);

            List<Comments> help = getAllOrders();
            for(int i=0; i<help.size()-1; i++)
            {
                if(help.get(i).getUser().getId().equals(createdComment.getUser().getId())){
                    Optional<Comments> existingCommentOpt = commentRepository.findById(help.get(i).getIdcm());
                    Comments existingComment = existingCommentOpt.get();
                    existingComment.setActive(false);
                    commentRepository.save(existingComment);
                }
            }
            return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("comments/status/{id}")
    public ResponseEntity<Comments> updateComment(@PathVariable Long id, MultipartHttpServletRequest request) throws Exception {

        Optional<Comments> existingCommentOpt = commentRepository.findById(id);
        if (!existingCommentOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String commentData = request.getParameter("comment");
        ObjectMapper objectMapper = new ObjectMapper();
        Comments receivedComment = objectMapper.readValue(commentData, Comments.class);
        Comments existingComment = existingCommentOpt.get();
        existingComment.setDeleted(receivedComment.isDeleted());
        existingComment.setActive(receivedComment.isActive());
        Comments savedComment = commentRepository.save(existingComment);

        return new ResponseEntity<>(savedComment, HttpStatus.OK);
    }


    @PostMapping("/newLike")
    public ResponseEntity<CommentsLikes> createCommentLike(@RequestBody LikeRequest likeRequest) {
        try {
            CommentsLikes commentsLikes = new CommentsLikes();
            User user = userRepository.findById(likeRequest.getIdu()).orElse(null);
            Comments comments = commentRepository.findById(likeRequest.getIdcm()).orElse(null);
            commentsLikes.setComments(comments);
            commentsLikes.setUser(user);
            commentsLikes.setActive(likeRequest.isActive());
            CommentsLikes createdCommentsLikes = commentLikesRepository.save(commentsLikes);

            return new ResponseEntity<>(createdCommentsLikes, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/likes/all")
    public List<CommentsLikes> getAllLikes() {
        return commentLikesRepository.findAll();
    }
    @PutMapping("likes/update/{id}")
    public ResponseEntity<CommentsLikes> updateLike(@PathVariable Long id, MultipartHttpServletRequest request) throws Exception {

        Optional<CommentsLikes> existingCommentsLikesOpt = commentLikesRepository.findById(id);
        if (!existingCommentsLikesOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String commentsLikesData = request.getParameter("likes");
        ObjectMapper objectMapper = new ObjectMapper();
        CommentsLikes receivedComment = objectMapper.readValue(commentsLikesData, CommentsLikes.class);
        CommentsLikes existingCommentsLikes = existingCommentsLikesOpt.get();
        existingCommentsLikes.setActive(receivedComment.isActive());
        CommentsLikes savedCommentLike = commentLikesRepository.save(existingCommentsLikes);

        return new ResponseEntity<>(savedCommentLike, HttpStatus.OK);
    }
}
