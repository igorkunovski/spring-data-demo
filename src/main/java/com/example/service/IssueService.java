package com.example.service;

import com.example.entity.*;
import com.example.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;


@Service
@EnableConfigurationProperties(ReaderProperties.class)
public class IssueService {

    private final BookService bookService;
    private final ReaderService readerService;
    private final IssueRepository issueRepository;
    private final ReaderProperties readerProperties;

    @Autowired
    public IssueService(BookService bookService, ReaderService readerService, IssueRepository issueRepository,
                        ReaderProperties readerProperties) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.issueRepository = issueRepository;
        this.readerProperties = readerProperties;
    }

    public void addNewOpenedIssue(Issue issue) {
        issueRepository.save(issue);
    }

    public List<Issue> getUserIssues(long id) {
        return issueRepository.findAll().stream()
                .filter(issue -> issue.getReaderId()==id)
                .toList();
    }

    public Issue issue(IssueRequest request) {
        if (bookService.findById(request.getBookId()).isEmpty()) {
            throw new NoSuchElementException("Не найдена книга с идентификатором \"" + request.getBookId() + "\"");
        }
        if (readerService.findById(request.getReaderId()).isEmpty()) {
            throw new NoSuchElementException("Не найден читатель с идентификатором \"" + request.getReaderId() + "\"");
        }

        // проверить, что у читателя лимит не превышает в 3 книги
        if (readerService.findById(request.getReaderId()).get().getBookList().size() >= readerProperties.getMaxAllowedBooks()) {
            throw new NotAcceptableStatusException("Превышен допустимы лимит книг");
        }

        return new Issue(request.getReaderId(), request.getBookId());
    }

    public void closeIssue(Issue issue) {

        issueRepository.findAll().stream()
                .filter(i -> Objects.equals(i.getId(), issue.getId()))
                .findFirst()
                .ifPresent(searched -> {searched.setReturnedDate(LocalDateTime.now().withNano(0));
                    issueRepository.save(searched); });
    }

    public Optional<Issue> findById(Long id) {
        return issueRepository.findById(id);
    }

    public List<Issue> getOpenedIssues() {
        return issueRepository.findAll().stream()
                .filter(issueEntity -> issueEntity.getReturnedDate()==null)
                .toList();
    }

    public List<Issue> findAll() {
        return issueRepository.findAll();
    }
}
