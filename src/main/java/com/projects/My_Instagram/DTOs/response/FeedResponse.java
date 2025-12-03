package com.projects.My_Instagram.DTOs.response;

import java.time.LocalDateTime;
import java.util.Date;

public class FeedResponse {
    private Long postId;
    private String caption;

    private LocalDateTime uploadedAt;

    private String imageUrl;
    private String username;
    private String profilePicUrl;
    private Integer noOfLikes;
    private Integer noOfComments;
    private Boolean likedByYou;

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public Integer getNoOfLikes() {
        return noOfLikes;
    }

    public void setNoOfLikes(Integer noOfLikes) {
        this.noOfLikes = noOfLikes;
    }

    public Integer getNoOfComments() {
        return noOfComments;
    }

    public void setNoOfComments(Integer noOfComments) {
        this.noOfComments = noOfComments;
    }

    public Boolean getLikedByYou() {
        return likedByYou;
    }

    public void setLikedByYou(Boolean likedByYou) {
        this.likedByYou = likedByYou;
    }
}
