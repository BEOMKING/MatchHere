package com.ssafy.match.chat.dao;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ssafy.match.chat.entity.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class ChatHistoryDao {
    // A simple cache for temporarily storing chat data
    private final Cache<UUID, ChatMessage> chatHistoryCache = CacheBuilder
            .newBuilder().maximumSize(20).expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public void save(ChatMessage chatObj) {
        this.chatHistoryCache.put(UUID.randomUUID(), chatObj);
    }

    public List<ChatMessage> get() {
        return chatHistoryCache.asMap().values().stream()
                .sorted(Comparator.comparing(ChatMessage::getSentTime))
                .collect(Collectors.toList());
    }
}
