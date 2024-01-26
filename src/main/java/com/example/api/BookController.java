package com.example.api;

import com.example.entity.BookEntity;
import com.example.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookEntity>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookEntity> getBook(@PathVariable Long id) {
        try {

            BookEntity book = bookService.getBookById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookEntity> deleteBook(@PathVariable Long id) {

        try {

            BookEntity book = bookService.getBookById(id);
            bookService.deleteBook(id);
            return new ResponseEntity<>(book, HttpStatus.NO_CONTENT);

        }catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<BookEntity> createBook(@RequestBody BookEntity book) {
        try {

            bookService.createBook(book);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
