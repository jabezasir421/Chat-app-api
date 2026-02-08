package com.example.chatapp.controller;

import com.example.chatapp.dto.ChatMessageRequest;
import com.example.chatapp.dto.ChatMessageResponse;
import com.example.chatapp.service.ChatMessageService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class ChatController {
  private final ChatMessageService chatMessageService;

  public ChatController(ChatMessageService chatMessageService) {
    this.chatMessageService = chatMessageService;
  }

  @MessageMapping("/chat.send")
  @SendTo("/topic/messages")
  public ChatMessageResponse sendMessage(@Valid ChatMessageRequest request) {
    return chatMessageService.saveMessage(request);
  }

  @GetMapping
  public List<ChatMessageResponse> getRecentMessages(
      @RequestParam(name = "limit", defaultValue = "50") int limit) {
    return chatMessageService.getRecentMessages(limit);
  }
}