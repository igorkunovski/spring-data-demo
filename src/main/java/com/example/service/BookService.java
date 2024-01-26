package com.example.service;

import com.example.entity.BookEntity;

import java.util.List;

public interface BookService {
    BookEntity getBookById(Long id);
    List<BookEntity> getAllBooks();
    void deleteBook(Long id);
    void createBook(BookEntity book);
}