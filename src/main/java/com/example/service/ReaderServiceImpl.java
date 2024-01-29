package com.example.service;

import com.example.entity.Book;
import com.example.entity.Reader;
import com.example.repository.ReaderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
//@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService{

    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @PostConstruct
    public void generateReader() {
        for (int i = 100; i < 111; i++){
            save(new Reader("Reader #" + i));
        }
    }

    @Override
    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Optional<Reader> findById(Long id) {
        return readerRepository.findById(id);
    }

    @Override
    public void save(Reader reader) {
        if (readerRepository.findAll().contains(reader)) {
            throw new NoSuchElementException("Такой читатель уже введен \"" + reader + "\"");
        } else {
            readerRepository.save(reader);
        }
    }

    @Override
    public void deleteById(Long id) {
        readerRepository.deleteById(id);
    }

    @Override
    public void addBook(Reader reader, Book book) {
        readerRepository.findById(reader.getId()).get().addBook(book);
    }
}
