package com.projects.My_Instagram.repositories;

import com.projects.My_Instagram.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdOrderByTimeAsc(Long chatId);

    List<Message> findByReceiverIdAndSeenFalse(Long receiverId);

    List<Message> findByChatId(Long chatId);
    Optional<Message> findTopByChatIdOrderByTimeDesc(Long chatId);
}

