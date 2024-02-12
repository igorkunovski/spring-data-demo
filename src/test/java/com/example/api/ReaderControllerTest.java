package com.example.api;

import com.example.JUnitSpringBootBase;
import com.example.entity.Book;
import com.example.entity.Reader;
import com.example.repository.ReaderRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ReaderControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    ReaderRepository readerRepository;

    @Data
    @NoArgsConstructor
    static class JUnitReader{
        private Long id;
        private String name;
        private List<Book> bookList;

        public JUnitReader(Long id, String name) {
            this.id = id;
            this.name = name;
            this.bookList = new ArrayList<>();
        }
    }

    @Test
    void getAllReaders() {
        readerRepository.saveAll(List.of(
                new Reader("reader"),
                new Reader("reader1")

        ));
        List<Reader> expected = readerRepository.findAll();

        List<ReaderControllerTest.JUnitReader> responseBody = webTestClient.get()
                .uri("/api/reader")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<ReaderControllerTest.JUnitReader>>() { })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());

        for (ReaderControllerTest.JUnitReader reader : responseBody) {
            boolean found = expected.stream()
                    .anyMatch(item -> Objects.equals(item.getName(), reader.getName()));
            Assertions.assertTrue(found);
        }
    }


    @Test
    void findByIdSuccess() {
        Reader expected = readerRepository.save(new Reader("reader2"));
        ReaderControllerTest.JUnitReader responseBody = webTestClient.get()
                .uri("/api/reader/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReaderControllerTest.JUnitReader.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getName(), responseBody.getName());
    }

    @Test
    void findByIdNotFound() {

        ReaderControllerTest.JUnitReader responseBody = webTestClient.get()
                .uri("/api/reader/" + -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ReaderControllerTest.JUnitReader.class)
                .returnResult().getResponseBody();

        assertNull(responseBody);
    }

    @Test
    void deleteReader() {
        Reader expected = readerRepository.save(new Reader("reader3"));
        ReaderControllerTest.JUnitReader responseBody = webTestClient.delete()
                .uri("/api/reader/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReaderControllerTest.JUnitReader.class)
                .returnResult().getResponseBody();

        assertNull(responseBody);
    }

    @Test
    void deleteReaderBadRequest() {
        ReaderControllerTest.JUnitReader responseBody = webTestClient.delete()
                .uri("/api/reader/" + -1)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ReaderControllerTest.JUnitReader.class)
                .returnResult().getResponseBody();

        assertNull(responseBody);
    }

    @Test
    void createReaderCreated(){
        ReaderControllerTest.JUnitReader request = new ReaderControllerTest.JUnitReader(1L,"reader4");

        ReaderControllerTest.JUnitReader responseBody = webTestClient.post()
                .uri("/api/reader")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ReaderControllerTest.JUnitReader.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());
        Assertions.assertEquals(responseBody.getId(), request.getId());

        Assertions.assertNotNull(responseBody.getName());
        Assertions.assertEquals(responseBody.getName(), request.getName());

        Assertions.assertTrue(readerRepository.findById(request.getId()).isPresent());
    }

    @Test
    void getReaderIssueList() {
        Reader expected = readerRepository.save(new Reader("reader5"));
        expected.addBook(new Book("book", "author"));

        List<BookControllerTest.JUnitBook> responseBody = webTestClient.get()
                .uri("/api/reader/" + expected.getId() +"/issue")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<BookControllerTest.JUnitBook>>() { })
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);

        for (BookControllerTest.JUnitBook book : responseBody) {
            boolean found = expected.getBookList().stream()
                    .filter(item -> Objects.equals(item.getTitle(), book.getTitle()))
                    .anyMatch(item -> Objects.equals(item.getAuthor(), book.getAuthor()));
            Assertions.assertTrue(found);
        }
    }
}