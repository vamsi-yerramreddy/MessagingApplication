package com.messaging.inbox.folders;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

@Table(value = "folders_by_user")

public class Folder {
    @PrimaryKeyColumn(name="user_id",ordinal = 0,type= PrimaryKeyType.PARTITIONED)
    private String id;

    @PrimaryKeyColumn(name="label",ordinal = 1, type=PrimaryKeyType.CLUSTERED)
    private String label;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String color;

    public String getId() {
        return id;
    }
    public Folder(){

    }

    public Folder(String Id, String label, String color) {
        this.id = Id;
        this.label = label;
        this.color = color;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
