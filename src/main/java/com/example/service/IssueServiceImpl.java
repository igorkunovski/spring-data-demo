package com.example.service;

import com.example.entity.IssueEntity;
import com.example.entity.IssueRequestEntity;
import com.example.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.NotAcceptableStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final BookService bookService;
    private final ReaderService readerService;
    private final IssueRepository issueRepository;

    @Autowired
    public IssueServiceImpl(BookService bookService, ReaderServiceImpl readerService, IssueRepository issueRepository) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.issueRepository = issueRepository;
    }

    @Value("${application.max-allowed-books:1}")
    private int maxAllowedBook;

    @Override
    public void addNewOpenedIssue(IssueEntity issue) {
        issueRepository.save(issue);
    }

    @Override
    public List<IssueEntity> getUserIssues(long id) {
        return issueRepository.findAll().stream()
                .filter(issue -> issue.getReaderId()==id)
                .toList();
    }

    @Override
    public IssueEntity issue(IssueRequestEntity request) {
        if (bookService.getBookById(request.getBookId()) == null) {
            throw new NoSuchElementException("Не найдена книга с идентификатором \"" + request.getBookId() + "\"");
        }
        if (readerService.getReaderById(request.getReaderId()) == null) {
            throw new NoSuchElementException("Не найден читатель с идентификатором \"" + request.getReaderId() + "\"");
        }

        // проверить, что у читателя лимит не превышает в 3 книги
        if (readerService.getReaderById(request.getReaderId()).getBookList().size() >= maxAllowedBook) {
            throw new NotAcceptableStatusException("Превышен допустимы лимит книг");
        }

        return new IssueEntity(request.getReaderId(), request.getBookId());
    }

    @Override
    public IssueEntity getIssueById(long id) {
        return issueRepository.findById(id).get();
    }

    @Override
    public void closeIssue(IssueEntity issue) {

        issueRepository.findAll().stream()
                .filter(i -> Objects.equals(i.getId(), issue.getId()))
                .findFirst()
                .ifPresent(searched -> {searched.setReturnedDate(LocalDateTime.now().withNano(0));
                    issueRepository.save(searched); });
    }

    public List<IssueEntity> getOpenedIssues() {
        return issueRepository.findAll().stream()
                .filter(issueEntity -> issueEntity.getReturnedDate()==null)
                .toList();
    }

    @Override
    public List<IssueEntity> getAllIssues() {
        return issueRepository.findAll();
    }
}
