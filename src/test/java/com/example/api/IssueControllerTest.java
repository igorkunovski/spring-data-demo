package com.example.api;

import com.example.JUnitSpringBootBase;
import com.example.entity.Book;
import com.example.entity.Issue;
import com.example.entity.Reader;
import com.example.repository.BookRepository;
import com.example.repository.IssueRepository;
import com.example.repository.ReaderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class IssueControllerTest extends JUnitSpringBootBase {

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    IssueRepository issueRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class JUnitIssue {

        private Long id;
        private Long readerId;
        private Long bookId;
        private LocalDateTime issueDate;
        private LocalDateTime returnedDate;

        public JUnitIssue(Long readerId, Long bookId) {
            this.readerId = readerId;
            this.bookId = bookId;

            this.issueDate = LocalDateTime.now().withNano(0);
            this.returnedDate = null;
        }

    }


    @Test
    void getAllIssues() {
        issueRepository.saveAll(List.of(
                new Issue(1L,1L),
                new Issue(2L, 2L)

        ));
        List<Issue> expected = issueRepository.findAll();


        List<IssueControllerTest.JUnitIssue> responseBody = webTestClient.get()
                .uri("/api/issue/all")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<IssueControllerTest.JUnitIssue>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());

        for (IssueControllerTest.JUnitIssue issue : responseBody) {
            boolean found = expected.stream()
                    .filter(item -> Objects.equals(item.getId(), issue.getId()))
                    .filter(item -> Objects.equals(item.getReaderId(), issue.getReaderId()))
                    .filter(item -> Objects.equals(item.getBookId(), issue.getBookId()))
                    .anyMatch(item -> Objects.equals(item.getIssueDate(), issue.getIssueDate()));
            Assertions.assertTrue(found);
        }
    }


    @Test
    void getOpenedIssues() {

        issueRepository.saveAll(List.of(
                new Issue(1L,1L),
                new Issue(2L, 2L)
        ));
        List<Issue> expected = issueRepository.findAll();

        expected.get(0).setReturnedDate(LocalDateTime.now());

        List<IssueControllerTest.JUnitIssue> responseBody = webTestClient.get()
                .uri("/api/issue/opened")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<IssueControllerTest.JUnitIssue>>() {
                })
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.size(), responseBody.size());

        for (IssueControllerTest.JUnitIssue issue : responseBody) {
            boolean found = expected.stream()
                    .filter(item -> Objects.equals(item.getId(), issue.getId()))
                    .filter(item -> Objects.equals(item.getReaderId(), issue.getReaderId()))
                    .filter(item -> Objects.equals(item.getBookId(), issue.getBookId()))
                    .anyMatch(item -> Objects.equals(item.getIssueDate(), issue.getIssueDate()));
            Assertions.assertTrue(found);
        }
    }


    @Test
    void findByIdSuccess() {
        Issue expected = issueRepository.save(new Issue(1L, 1L));

        IssueControllerTest.JUnitIssue responseBody = webTestClient.get()
                .uri("/api/issue/" + expected.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(IssueControllerTest.JUnitIssue.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(expected.getId(), responseBody.getId());
        Assertions.assertEquals(expected.getReaderId(), responseBody.getReaderId());
        Assertions.assertEquals(expected.getBookId(), responseBody.getBookId());
        Assertions.assertEquals(expected.getIssueDate(), responseBody.getIssueDate());
    }

    @Test
    void findByIdNotFound() {

        JUnitIssue responseBody = webTestClient.get()
                .uri("/api/issue/" + -1)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(JUnitIssue.class)
                .returnResult().getResponseBody();

        assertNull(responseBody);
    }

    @Test
    void issueBook() {

        Book book = bookRepository.save(new Book(1L,"book", "author"));
        Reader reader = readerRepository.save(new Reader(1L, "reader"));

        Issue request = issueRepository.save(new Issue(reader.getId(), book.getId()));

        IssueControllerTest.JUnitIssue responseBody = webTestClient.post()
                .uri("/api/issue")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(IssueControllerTest.JUnitIssue.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertNotNull(responseBody.getId());

        Assertions.assertNotNull(responseBody.getBookId());
        Assertions.assertEquals(responseBody.getBookId(), request.getBookId());

        Assertions.assertNotNull(responseBody.getReaderId());
        Assertions.assertEquals(responseBody.getReaderId(), request.getReaderId());

        Assertions.assertNotNull(responseBody.getIssueDate());

        assertNull(responseBody.getReturnedDate());
        Assertions.assertEquals(responseBody.getReturnedDate(), request.getReturnedDate());

        Assertions.assertTrue(issueRepository.findById(request.getId()).isPresent());
    }

    @Test
    void returnBook() {
        Book book = bookRepository.save(new Book(1L,"book", "author"));
        Reader reader = readerRepository.save(new Reader(1L, "reader"));

        Issue request = issueRepository.save(new Issue(reader.getId(), book.getId()));

        IssueControllerTest.JUnitIssue responseBody = webTestClient.put()
                .uri("/api/issue/" + request.getId())
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(IssueControllerTest.JUnitIssue.class)
                .returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);

        Assertions.assertNotNull(responseBody.getId());

        Assertions.assertNotNull(responseBody.getBookId());
        Assertions.assertEquals(responseBody.getBookId(), request.getBookId());

        Assertions.assertNotNull(responseBody.getReaderId());
        Assertions.assertEquals(responseBody.getReaderId(), request.getReaderId());

        Assertions.assertNotNull(responseBody.getIssueDate());
        Assertions.assertNotNull(responseBody.getReturnedDate());

        Assertions.assertTrue(issueRepository.findById(request.getId()).isPresent());
    }
}