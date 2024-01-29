package com.example.service;

import com.example.entity.Book;
import com.example.entity.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderService {
    List<Reader> findAll();
    Optional<Reader> findById(Long id);
    void deleteById(Long id);
    void save(Reader reader);
    void addBook(Reader reader, Book book);
}
