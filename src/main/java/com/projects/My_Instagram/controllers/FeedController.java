package com.projects.My_Instagram.controllers;

import com.projects.My_Instagram.DTOs.response.FeedResponse;
import com.projects.My_Instagram.services.FeedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feed")
    public List<FeedResponse> fetchFeed(){
        return feedService.fetchFeed();
    }
}
