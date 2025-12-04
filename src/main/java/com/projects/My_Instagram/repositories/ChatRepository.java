package com.projects.My_Instagram.repositories;

import com.projects.My_Instagram.models.Chat;
import com.projects.My_Instagram.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
    Optional<Chat> findByUser2IdAndUser1Id(Long user1Id, Long user2Id);
    List<Chat> findByUser1Id(Long userId);
    List<Chat> findByUser2Id(Long userId);
}
