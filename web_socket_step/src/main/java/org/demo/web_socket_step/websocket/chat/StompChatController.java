package org.demo.web_socket_step.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final StompChatService stompChatService;

    // 1. 채팅방 화면(기존 글 목록 포함)
    // http://localhost:8080/stomp/chat
    @GetMapping("/stomp/chat")
    public String index(Model model) {
        model.addAttribute("chatList", stompChatService.findAll());
        return "stomp/index";
    }

    /**
     * 2. 메세지 수신 (Publish 처리)
     * - 클라이언트가 /pub/chat/message 로 메세지를 보내면 이 메서드가 실행 됨
     * - @MessageMapping: 웹 소켓 메시지 라우팅 (HTTP @RequestMapping 과 유사)
     * - registry.setApplicationDestinationPrefixes("/pub"); 프리픽스 설정 해둠
     */
    @MessageMapping("/chat/message")
    public void receiveMessage(Map<String, String> payload) {
        // DTO 대신 Map 을 사용하여 JSON 데이터를 받음
        String sender = payload.get("sender");
        String message = payload.get("message");

        // DB에 알아서 저장하고 알아서 방송함
        stompChatService.saveAndBroadcast(message, sender);
    }
}
