package com.projects.My_Instagram.services;

import com.projects.My_Instagram.DTOs.response.CommentResponse;
import com.projects.My_Instagram.DTOs.response.FeedResponse;
import com.projects.My_Instagram.exceptions.PostNotFoundException;
import com.projects.My_Instagram.helper.UserUtils;
import com.projects.My_Instagram.models.Comment;
import com.projects.My_Instagram.models.Post;
import com.projects.My_Instagram.models.User;
import com.projects.My_Instagram.repositories.CommentRepository;
import com.projects.My_Instagram.repositories.PostRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.projects.My_Instagram.constants.exception.ExceptionMessages.POST_NOT_FOUND;
import static com.projects.My_Instagram.helper.Helper.formCommentResponse;

@Service
public class FeedService {
    private final UserUtils userUtils;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public FeedService(UserUtils userUtils, PostRepository postRepository, CommentRepository commentRepository) {
        this.userUtils = userUtils;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Cacheable
    public List<FeedResponse> fetchFeed() {
        User currectUser = userUtils.fetchCurrectUser();
        List<Post> posts = new ArrayList<>();
        for (User user : currectUser.getFollowing()) {
            posts.addAll(user.getPosts());
        }

        List<FeedResponse> feedResponses = new ArrayList<>();
        for (Post post : posts) {
            FeedResponse feedResponse = formFeedResponse(post, currectUser);
            feedResponses.add(feedResponse);
        }

        return feedResponses;
    }

    private FeedResponse formFeedResponse(Post post, User currectUser) {
        FeedResponse feedResponse = new FeedResponse();
        feedResponse.setPostId(post.getId());
        feedResponse.setCaption(post.getCaption());
        feedResponse.setUploadedAt(post.getUploadedAt());
        feedResponse.setImageUrl(post.getImageUrl());
        feedResponse.setUsername(post.getUser().getUsername());
        feedResponse.setProfilePicUrl(post.getUser().getProfilePicUrl());
        feedResponse.setNoOfLikes(post.getLikedUsers().size());
        feedResponse.setNoOfComments(getAllComments(post.getId()).size());
        feedResponse.setLikedByYou(post.getLikedUsers().contains(currectUser));

        return feedResponse;
    }

    public List<CommentResponse> getAllComments(Long postId) {
        List<CommentResponse> comments = new ArrayList<>();

        if(postRepository.findById(postId).isEmpty()){
            throw new PostNotFoundException(POST_NOT_FOUND.getMessage());
        }

        for (Comment comment : commentRepository.findByCommentedPostId(postId)) {
            comments.add(formCommentResponse(comment));
        }

        return comments;
    }
}
