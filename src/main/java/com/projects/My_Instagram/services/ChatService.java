package com.projects.My_Instagram.services;

import com.projects.My_Instagram.DTOs.response.ChatResponse;
import com.projects.My_Instagram.DTOs.response.MessageResponse;
import com.projects.My_Instagram.DTOs.response.SendMessageResponse;
import com.projects.My_Instagram.constants.exception.ExceptionMessages;
import com.projects.My_Instagram.exceptions.AccessDeniedException;
import com.projects.My_Instagram.exceptions.AppException;
import com.projects.My_Instagram.helper.UserUtils;
import com.projects.My_Instagram.models.Chat;
import com.projects.My_Instagram.models.Message;
import com.projects.My_Instagram.models.User;
import com.projects.My_Instagram.repositories.ChatRepository;
import com.projects.My_Instagram.repositories.MessageRepository;
import com.projects.My_Instagram.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final UserUtils userUtils;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public ChatService(UserUtils userUtils, UserRepository userRepository, ChatRepository chatRepository, MessageRepository messageRepository) {
        this.userUtils = userUtils;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    public Chat createChat(User user1, User user2) {
        Optional<Chat> chat =
                chatRepository.findByUser1IdAndUser2Id(user1.getId(), user2.getId())
                        .or(() -> chatRepository.findByUser1IdAndUser2Id(user2.getId(), user1.getId()));


        return chat.orElseGet(() -> createNewChat(user1, user2));
    }

    private Chat createNewChat(User user1, User user2) {
        Chat chat = new Chat();
        chat.setUser1(user1);
        chat.setUser2(user2);
        chat.setCreatedAt(LocalDateTime.now());
        chat.setMessages(new ArrayList<>());
        return chatRepository.save(chat);
    }

    public SendMessageResponse sendMessage(String username, String content) {
        User currentUser = userUtils.fetchCurrectUser();
        User targetUser = userUtils.fetchUser(username);
        Message message = createMessage(content, targetUser, currentUser);
        Message savedMessage = messageRepository.save(message);

        return formSendMessageResponse(savedMessage);
    }

    private SendMessageResponse formSendMessageResponse(Message savedMessage) {
        SendMessageResponse sendMessageResponse = new SendMessageResponse();
        sendMessageResponse.setMessageId(savedMessage.getId());
        sendMessageResponse.setChatId(savedMessage.getChat().getId());
        sendMessageResponse.setContent(savedMessage.getContent());
        sendMessageResponse.setReceiver(savedMessage.getReceiver().getUsername());
        sendMessageResponse.setSentAt(savedMessage.getTime());

        return sendMessageResponse;
    }

    private Message createMessage(String content, User targetUser, User currentUser) {
        Message message = new Message();
        message.setContent(content);
        message.setReceiver(targetUser);
        message.setSender(currentUser);
        message.setTime(LocalDateTime.now());
        message.setSeen(false);
        message.setChat(createChat(currentUser, targetUser));

        return message;
    }

    public List<MessageResponse> fetchMessages(String username) {
        User currentUser = userUtils.fetchCurrectUser();
        User targetUser = userUtils.fetchUser(username);
        Chat chat = createChat(currentUser, targetUser);
        List<MessageResponse> messages = new ArrayList<>();

        for (Message message : chat.getMessages()) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setContent(message.getContent());
            messageResponse.setSender(message.getSender().getUsername());
            messageResponse.setSentAt(message.getTime());
            messages.add(messageResponse);
        }

        return messages;
    }

    public String markMessagesAsSeen(Long chatId) {
        User currentUser = userUtils.fetchCurrectUser();

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new AppException(ExceptionMessages.CHAT_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        if (!chat.getUser1().getId().equals(currentUser.getId()) &&
                !chat.getUser2().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to view this chat");
        }

        List<Message> messages = messageRepository.findByChatId(chatId);

        for (Message msg : messages) {
            if (!msg.getSender().getId().equals(currentUser.getId())) {
                msg.setSeen(true);
            }
        }

        messageRepository.saveAll(messages);

        return "All messages marked as seen";
    }

    public List<ChatResponse> fetchChats() {

        User currentUser = userUtils.fetchCurrectUser();

        List<Chat> chats = new ArrayList<>();
        chats.addAll(chatRepository.findByUser1Id(currentUser.getId()));
        chats.addAll(chatRepository.findByUser2Id(currentUser.getId()));

        return chats.stream()
                .map(chat -> {
                    User otherUser = chat.getUser1().getId().equals(currentUser.getId())
                            ? chat.getUser2()
                            : chat.getUser1();

                    return formChatResponse(otherUser, chat);
                })
                .sorted(
                        Comparator.comparing(
                                ChatResponse::getLastMessageTime,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                )
                .toList();

    }

    private ChatResponse formChatResponse(User otherUser, Chat chat) {

        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setUsername(otherUser.getUsername());
        chatResponse.setProfilePicUrl(otherUser.getProfilePicUrl());
        chatResponse.setChatId(chat.getId());

        Optional<Message> lastMessageOpt = messageRepository.findTopByChatIdOrderByTimeDesc(chat.getId());

        if (lastMessageOpt.isPresent()) {
            Message lastMessage = lastMessageOpt.get();
            chatResponse.setLastMessage(lastMessage.getContent());
            chatResponse.setLastMessageTime(lastMessage.getTime());
        } else {
            chatResponse.setLastMessage(null);
            chatResponse.setLastMessageTime(null);
        }

        return chatResponse;
    }

}
