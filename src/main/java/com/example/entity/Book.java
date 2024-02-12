package com.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Schema(name = "Book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Id of the book")
    private Long id;

    @Column(name = "title")
    @Schema(name = "Title of the book")
    private String title;

    @Column(name = "author")
    @Schema(name = "Author of the book")
    private String author;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }
}
