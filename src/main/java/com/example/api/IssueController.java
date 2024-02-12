package com.example.api;

import com.example.entity.*;
import com.example.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name="Issue")
@RequestMapping("/api/issue")
public class IssueController {

    private final IssueService issueService;
    private final ReaderService readerService;
    private final BookService bookService;

    @Autowired
    public IssueController(IssueService service, ReaderService readerService, BookService bookService) {
        this.issueService = service;
        this.readerService = readerService;
        this.bookService = bookService;
    }

    /**
     *
     * @return возврат списка только открытых выдач
     */
    @GetMapping("/opened")
    @Operation(summary = "get open issues", description = "load opened issues from repository")
    public ResponseEntity<List<Issue>> getOpenedIssues() {
        return new ResponseEntity<>(issueService.getOpenedIssues(), HttpStatus.OK);
    }

    /**
     *
     * @return возврат списка всех выдач (и открытых и закрытых)
     */

    @GetMapping("/all")
    @Operation(summary = "get all issues", description = "load all issues from repository")
    public ResponseEntity<List<Issue>> getAllIssues() {
        return new ResponseEntity<>(issueService.findAll(), HttpStatus.OK);
    }

    /**
     *
     * @param id - идентификатор выдачи
     * @return - тело выдачи и статус запроса
     */
    @GetMapping("/{id}")
    @Operation(summary = "find by id", description = "search issue from repository by its id")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    public ResponseEntity<Issue> getIssue(@PathVariable long id) {
        Optional<Issue> issue = issueService.findById(id);
        return issue.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     *
     * @param request - тело запроса на добавление
     * @return - статус запроса и тело запроса
     */

    @PostMapping
    @Operation(summary = "create new issue", description = "create new issue and save it to repository")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    public ResponseEntity<Issue> issueBook(@RequestBody IssueRequest request) {
        log.info("Получен запрос на выдачу: readerId = {}, bookId = {}", request.getReaderId(), request.getBookId());

        final Issue issue;

        try {
            issue = issueService.issue(request);

            // добавление книги в список взятых книг читателя
            Optional<Reader> reader = readerService.findById(request.getReaderId());
            Optional<Book> book = bookService.findById(request.getBookId());
            if (reader.isPresent() && book.isPresent()) {
                readerService.addBook(reader.get(), book.get());
                issueService.addNewOpenedIssue(issue);
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        catch (NotAcceptableStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(issue);
    }

    /**
     * Метод добавляет текущее время в поле returnedDate выдачи и
     * удаляет книгу из списка взятых книг читателем.
     *
     * @param id - id выдачи для возврата.
     * @return - статус запроса и тело запроса
     */
    @PutMapping("/{id}")
    @Operation(summary = "close issue", description = "puts return date to issue and closes it, removes issue book from reader's list")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400")
    public ResponseEntity<Issue> returnBook(@PathVariable long id) {

        Optional<Issue> issue = issueService.findById(id);
        if (issue.isPresent()) {
            Optional<Book> book = bookService.findById(issue.get().getBookId());

            // книга удаляется из списка взятых книг читателя
            readerService.findById(issue.get().getReaderId()).get().removeBook(book.get());

            //добавляется время возврата книги
            issueService.closeIssue(issue.get());

            return ResponseEntity.status(HttpStatus.OK).body(issueService.findById(id).get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
