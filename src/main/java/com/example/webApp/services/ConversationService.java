package com.example.webApp.services;

import com.example.webApp.models.Conversation;
import com.example.webApp.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import repositories.ConversationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationService {
//    private final MongoTemplate mongoTemplate;


    @Autowired
    private ConversationRepository repository;

    //    public ConversationService(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//    public List<Conversation> findByBothIdsInMessages(String id1, String id2) {
//        MatchOperation matchStage = Aggregation.match(
//                Criteria.where("messages.sender").in(id1, id2)
//                        .and("messages.receiver").in(id1, id2)
//        );
//
//        Aggregation aggregation = Aggregation.newAggregation(matchStage);
//
//        return mongoTemplate.aggregate(aggregation, "Conversation", Conversation.class)
//                .getMappedResults();
//    }
    public void addMessageToDocument(Message message) {
        List<String> list = new ArrayList<>();
        list.add(message.getReceiver());
        list.add(message.getSender());
        List<Conversation> conversationList = repository.findByMembersIn(list);
        for (Conversation c :
                conversationList) {
            if (c.getMembers().contains(message.getReceiver()) && c.getMembers().contains(message.getSender())) {
                //if exist document desire
                // if they are first time chatting => need to create a new conversation

                if (c == null) {
                    //add message
                    List<Message> messageList = new ArrayList<Message>();
                    messageList.add(message);
                    //add members
                    List<String> members = new ArrayList<>();
                    members.add(message.getReceiver());
                    members.add(message.getSender());
                    //init conversation
                    Conversation conversation = new Conversation();
                    //add to the conversation
                    conversation.setTime(String.valueOf(LocalDateTime.now()));
                    conversation.setMessages(messageList);
                    conversation.setMembers(members);
                    //save new conversation
                    repository.save(conversation);
                } else {
                    List<Message> messages = c.getMessages();
                    message.setTimestamp(String.valueOf(LocalDateTime.now()));
                    messages.add(message);
                    c.setTime(String.valueOf(LocalDateTime.now()));
                    c.setMessages(messages);
                    repository.save(c);
                }
            }
        }


    }
}
