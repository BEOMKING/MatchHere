//package com.ssafy.match.chat.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ssafy.match.chat.dao.MessageRepository;
//import com.ssafy.match.chat.dto.ChatMessage;
//import java.util.HashMap;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//
//@AllArgsConstructor
//@Service
//public class ChatReceiverServiceImpl {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ChatReceiverServiceImpl.class);
//
////    @Autowired
//    private SimpMessagingTemplate template;
//
//    //@KafkaListener 어노테이션 부착 시 -> 받아올 메세지가 있을 때 수행하는 일들을 아래 메서드에 적용한다.
////    @KafkaListener(topics = "test1")
//    public void receive(/*@Payload*/ ChatMessage message) throws Exception {
//        System.out.println("socket1");
//
//        LOGGER.info("message='{}'", message);
//        System.out.println(message.getContent());
//        System.out.println("socket2");
//
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println("socket3");
//
//        String json = mapper.writeValueAsString(message);
//        System.out.println("socket4");
//
//        // 프론트의Stringify와 유사
//        StringBuilder destSocket = new StringBuilder("/sub");
//        destSocket.append('/').append(message.getSenderId());
//        System.out.println("socket5");
//
//        StringBuilder departSocket = new StringBuilder("/sub");
//        departSocket.append('/').append(message.getReceiverId());
//        System.out.println("socket6");
//
//        this.template.convertAndSend(destSocket.toString(), json);
//        System.out.println("socket7");
//
//        this.template.convertAndSend(departSocket.toString(), json);
//        System.out.println("socket8");
//
//        // 실제 socket으로 메세지를 전달하는 메서드
//    }
//}
