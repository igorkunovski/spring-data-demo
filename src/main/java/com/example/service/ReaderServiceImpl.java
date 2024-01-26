package com.example.service;

import com.example.entity.BookEntity;
import com.example.entity.ReaderEntity;
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
            createReader("Reader #" + i);
        }
    }

    @Override
    public List<ReaderEntity> getAllReaders() {
        return readerRepository.findAll();
    }

    @Override
    public ReaderEntity getReaderById(Long id) {
        return readerRepository.findById(id).get();
    }

    @Override
    public void createReader(String name) {

        if (findReaderByName(name)) {
            throw new NoSuchElementException("Такой читатель уже введен \"" + name + "\"");

        } else {
            readerRepository.save(new ReaderEntity(name));
        }
    }

    @Override
    public void deleteReaderById(Long id) {
        readerRepository.deleteById(id);
    }

    @Override
    public void addBook(ReaderEntity reader, BookEntity book) {
        readerRepository.findById(reader.getId()).get().addBook(book);
    }

    @Override
    public boolean findReaderByName(String name) {
        {
            Optional<ReaderEntity> first = readerRepository.findAll().stream()
                    .filter(reader -> reader.getName().equals(name))
                    .findFirst();
            return first.isPresent();
        }
    }
}
