package com.messaging.inbox.emaillist;

import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table(value="messages_by_user_folder")
public class EmailListItem {

    @PrimaryKey
    private EmailListItemKey key;

    @CassandraType(type= CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> cc;
    @CassandraType(type = CassandraType.Name.LIST,typeArguments = CassandraType.Name.TEXT)
    private List<String> to;

    @CassandraType(type= CassandraType.Name.TEXT)
    private String subject;

    @CassandraType(type= CassandraType.Name.BOOLEAN)
    private boolean isUnread;

    @Transient
    private String agoTimeString;


    //Getters and Setters
    public void setAgoTimeString(String time){
        this.agoTimeString=time;
    }
    public String getAgoTimeString(){
        return agoTimeString;
    }

    public EmailListItemKey getKey() {
        return key;
    }

    public void setKey(EmailListItemKey key) {
        this.key = key;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isUnread() {
        return isUnread;
    }

    public void setUnread(boolean unread) {
        isUnread = unread;
    }
}
