package com.example.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Creat by liuchunbo 2023/4/8
 */
//这个实例表示mongodb中一条文档
@Document(collection = "productsTest")
public class Book {
    @Id //id映射文档中_id
    private Integer id;
    @Field("username")
    private String name;
    @Field
    private String type;
    @Field
    private String description;

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\'' + ", description='"
            + description + '\'' + '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
