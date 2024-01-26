package com.example.model;

import lombok.Data;

@Data

 public class Book {

    private Long id;
    private String title;
    private String author;


    public Book(String title, String author) {

        this.title = title;
        this.author = author;
    }
}
