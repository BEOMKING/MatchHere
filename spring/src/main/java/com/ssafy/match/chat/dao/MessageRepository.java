//package com.ssafy.match.chat.dao;
//
//import com.ssafy.match.chat.config.TuningValuesConfig;
//import com.ssafy.match.chat.dto.ChatMessage;
//import java.sql.Timestamp;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.transaction.annotation.Transactional;
//
//public interface MessageRepository extends JpaRepository<ChatMessage, Integer> {
//    @Query(value =
//        "select * from matching.message where (sender_id = ?1 or receiver_id = ?1) and (sender_id = ?2 or receiver_id = ?2) order by id ASC LIMIT "
//            + TuningValuesConfig.CACHE_SIZE ,
//        nativeQuery = true)
//    List<ChatMessage> findBySenderIdOrReceiverId(long id1, long id2);
//
//    @Query(value =
//        "select * from matching.message where id < ?3 and (sender_id = ?1 or receiver_id = ?1) and (sender_id = ?2 or receiver_id = ?2) order by id DESC LIMIT "
//            + TuningValuesConfig.CACHE_SIZE ,
//        nativeQuery = true)
//    List<ChatMessage> findMoreMessageBySenderIdOrReceiverId(long id1, long id2, long msgPk);
//
//    @Query(value = ""
//        + "SELECT * FROM ( "
//            + "SELECT * FROM matching.message WHERE (sender_id, receiver_id, id) IN "
//                + "( SELECT sender_id, receiver_id, max(id) AS id "
//                    + "FROM matching.message "
//                    + "WHERE type != 2 AND (sender_id = ?1 OR receiver_id = ?1)"
//                    + "GROUP BY sender_id, receiver_id "
//                + ") "
//        + ") t "
//        + "group by t.id, t.sender_id, t.receiver_id; ",
//        nativeQuery = true)
//    List<ChatMessage> findSessions(long id);
//
////        List<ChatMessage> findTop
////     select * from matching.message where str_to_date(read_time, "%Y-%m-%d %H:%i:%s") < "1970-01-01 09:00:02" AND PK <  ;
//    @Transactional // update 실행시 필수
//    @Modifying(clearAutomatically = true)
//    @Query(value =
//        "update matching.message "
//            + "set read_time = ?1 "
//            + "WHERE receiver_id = ?2 AND sender_id = ?3 "
//            + "AND str_to_date(read_time, \"%Y-%m-%d %H:%i:%s\") < \"1970-01-01 09:00:03\" "
//            + "AND sent_time <= ?1 ",
//        nativeQuery = true
//    )
//    void updateRead(Timestamp read_time, long id1, long id2);
//}
