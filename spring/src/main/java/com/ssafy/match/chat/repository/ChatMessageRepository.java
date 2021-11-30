package com.ssafy.match.chat.repository;


import com.ssafy.match.chat.dto.ChatMessageInterface;
import com.ssafy.match.chat.entity.ChatMessage;
import com.ssafy.match.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    @Query(value = "select mm.content as content , mm.sent_time as sent_time, mm.nickname as nickname, mm.sender_id as sender_id from matching.chat_message mm where mm.chatRoom = :chatRoom")
//    List<ChatMessageInterface> findAllByRoom(@Param("chatRoom") ChatRoom chatRoom);

//    Page<ChatMessage> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);
    Page<ChatMessage> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);
    ChatMessage findTopByChatRoomOrderBySentTimeDesc(ChatRoom chatRoom);
}
