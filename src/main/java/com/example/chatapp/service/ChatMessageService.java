package com.example.chatapp.service;

import com.example.chatapp.dto.ChatMessageRequest;
import com.example.chatapp.dto.ChatMessageResponse;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.repo.ChatMessageRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;

  public ChatMessageService(ChatMessageRepository chatMessageRepository) {
    this.chatMessageRepository = chatMessageRepository;
  }

  @Transactional
  public ChatMessageResponse saveMessage(ChatMessageRequest request) {
    ChatMessage message = new ChatMessage(request.getSenderId(), request.getContent());
    message.setCreatedAt(Instant.now());
    ChatMessage saved = chatMessageRepository.save(message);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<ChatMessageResponse> getRecentMessages(int limit) {
    int size = Math.max(1, Math.min(limit, 200));
    var page = chatMessageRepository.findAllByOrderByCreatedAtDesc(
        PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    return page.stream().map(this::toResponse).collect(Collectors.toList());
  }

  private ChatMessageResponse toResponse(ChatMessage message) {
    return new ChatMessageResponse(
        message.getId(),
        message.getSenderId(),
        message.getContent(),
        message.getCreatedAt());
  }
}