package com.projects.My_Instagram.DTOs.response;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponse{
    private Long id;
    private String caption;
    private String imageUrl;
    private LocalDateTime uploadedAt;

    public List<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<String> likedUsers) {
        this.likedUsers = likedUsers;
    }

    private List<String> likedUsers;
    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private UserResponse user;
}
