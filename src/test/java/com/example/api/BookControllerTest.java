package com.example.api;

import com.example.JUnitSpringBootBase;
import com.example.entity.Book;
import com.example.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    BookRepository bookRepository;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class JUnitBook{
        private Long id;
        private String title;
        private String author;
    }

    @Test
    void getAllBooks() {

        bookRepository.saveAll(List.of(
                new Book("book", "author"),
                new Book("book4", "author4")

        ));
        List<Book> expected = bookRepository.findAll();

                List<JUnitBook> responseBody = webTestClient.get()
                .uri("/api/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<JUnitBook>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());

        for (JUnitBook book : responseBody) {
            boolean found = expected.stream()
                    .filter(item -> Objects.equals(item.getTitle(), book.getTitle()))
                    .anyMatch(item -> Objects.equals(item.getAuthor(), book.getAuthor()));
            Assertions.assertTrue(found);
        }
    }

    @Test
    void findByIdSuccess() {
        Book expected = bookRepository.save(new Book("book1", "author1"));
        JUnitBook responseBody = webTestClient.get()
                .uri("/api/book/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getTitle(), responseBody.getTitle());
        Assertions.assertEquals(expected.getAuthor(), responseBody.getAuthor());
    }

    @Test
    void findByIdNotFound() {

        JUnitBook responseBody = webTestClient.get()
                .uri("/api/book/" + -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        assertNull(responseBody);
    }

    @Test
    void deleteBook() {
        Book expected = bookRepository.save(new Book("book2", "author2"));
        JUnitBook responseBody = webTestClient.delete()
                .uri("/api/book/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getTitle(),  responseBody.getTitle());
        Assertions.assertEquals(expected.getAuthor(),  responseBody.getAuthor());
    }

    @Test
    void deleteBookBadRequest() {
        JUnitBook responseBody = webTestClient.delete()
                .uri("/api/book/" + -1)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        assertNull(responseBody);
    }

    @Test
    void createBook() {

        JUnitBook request = new JUnitBook(1L,"book3", "author3");

        JUnitBook responseBody = webTestClient.post()
                .uri("/api/book")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(JUnitBook.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(responseBody.getId(), request.getId());

        Assertions.assertNotNull(responseBody.getTitle());
        Assertions.assertEquals(responseBody.getTitle(), request.getTitle());

        Assertions.assertNotNull(responseBody.getAuthor());
        Assertions.assertEquals(responseBody.getAuthor(), request.getAuthor());

        Assertions.assertTrue(bookRepository.findById(request.getId()).isPresent());

    }
}