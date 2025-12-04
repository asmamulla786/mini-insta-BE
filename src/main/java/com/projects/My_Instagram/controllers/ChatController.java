package com.projects.My_Instagram.controllers;

import com.projects.My_Instagram.DTOs.request.SendMessageRequest;
import com.projects.My_Instagram.DTOs.response.ChatResponse;
import com.projects.My_Instagram.DTOs.response.MessageResponse;
import com.projects.My_Instagram.DTOs.response.SendMessageResponse;
import com.projects.My_Instagram.models.Chat;
import com.projects.My_Instagram.models.User;
import com.projects.My_Instagram.services.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/{username}/messages")
    public SendMessageResponse sendMessage(@PathVariable String username, @RequestBody SendMessageRequest request){
        return chatService.sendMessage(username,request.getContent());
    }

    @GetMapping("/{username}/messages")
    public List<MessageResponse> getMessages(@PathVariable String username){
        return chatService.fetchMessages(username);
    }

    @PatchMapping("/{chatId}/seen")
    public String markMessagesAsSeen(@PathVariable Long chatId){
        return chatService.markMessagesAsSeen(chatId);
    }

    @GetMapping
    public List<ChatResponse> getChats(){
        return chatService.fetchChats();
    }
}
