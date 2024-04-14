package com.example.webApp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Document("Conversation")
public class Conversation {
    @Id
    private String id; // MongoDB specific $oid type
    private String time;
    private List<String> members;
    private List<Message> messages;


    public Conversation(){

    }
//    public Conversation(String time, List<String> members, List<Message> messages) {
//        this.time = time;
//        this.members = members;
//        this.messages = messages;
//    }

    // Getters and setters
    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}