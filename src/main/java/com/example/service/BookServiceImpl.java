package com.example.service;

import com.example.entity.BookEntity;
import com.example.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
//@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void generateBook() {
        for (int i = 100; i < 111; i++){
            createBook(new BookEntity(("Book #" +i), ("Author #" +i)));
        }
    }

    public BookEntity getBookById(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new NoSuchElementException("Не найдена книга с идентификатором \"" + id + "\"");
        }
        return bookRepository.findById(id).get();
    }

    public void deleteBook(Long id) {
        getBookById(id);
        bookRepository.deleteById(id);
    }

    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public void createBook(BookEntity book) {

        for (BookEntity b: bookRepository.findAll()) {
            if (b.getTitle().equals(book.getTitle())&& b.getAuthor().equals(book.getAuthor())){
                throw new NoSuchElementException("Такая книга уже существует \"" + book + "\"");
            }
        }
        bookRepository.save(book);
    }
}