package com.example.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "readers")
@Data
@NoArgsConstructor
public class ReaderEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "booklist")
    @OneToMany
    private List<BookEntity> bookList;

    @Column(name = "name")
    private String name;


    public ReaderEntity(String name) {
        this.name = name;
        this.bookList = new ArrayList<>();
    }

    public void addBook(BookEntity book){
        bookList.add(book);
    }

    public void removeBook(BookEntity book){
        bookList.remove(book);
    }
}

