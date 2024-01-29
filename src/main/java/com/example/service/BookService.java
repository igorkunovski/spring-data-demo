package com.example.service;

import com.example.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findAll();
    void deleteById(Long id);
    void save(Book book);
    Optional<Book> findById(Long id);
}