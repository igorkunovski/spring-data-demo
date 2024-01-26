package com.example.service;

import com.example.entity.BookEntity;
import com.example.entity.ReaderEntity;

import java.util.List;

public interface ReaderService {
    List<ReaderEntity> getAllReaders();
    ReaderEntity getReaderById(Long id);
    void createReader(String name);
    void deleteReaderById(Long id);
    void addBook(ReaderEntity reader, BookEntity book);
    boolean findReaderByName(String name);
}
