//package com.ssafy.match.chat.dto;
//
//import java.io.Serializable;
//import java.sql.Timestamp;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Getter
//@Setter
//@Entity(name = "matching.message")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString
//public class ChatMessage implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
//    /*
//    * 1 => 일반 메세지
//    * 2 => 읽음 메세지
//    * */
//    int type;
//
//    @Column(name = "sender_id")
//    long senderId;
//    @Column(name = "receiver_id")
//    long receiverId;
//    Timestamp sent_time;
//    Timestamp read_time;
//    String content;;
//}
