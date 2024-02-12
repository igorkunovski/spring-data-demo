package com.example.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
@Schema(name = "Issue")
@Data
@NoArgsConstructor
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Id of the issue")
    private Long id;

    @Column(name = "readerId")
    @Schema(name = "Id of the reader")
    private Long readerId;

    @Column(name = "bookId")
    @Schema(name = "Id of the book")
    private Long bookId;
    @Column(name = "issueDate")
    @Schema(name = "Issue generation date")
    private LocalDateTime issueDate;
    @Column(name = "returnedDate")
    @Schema(name = "Issue close date")
    private LocalDateTime returnedDate;


    public Issue(Long readerId, Long bookId) {
        this.readerId = readerId;
        this.bookId = bookId;

        this.issueDate = LocalDateTime.now().withNano(0);
        this.returnedDate = null;
    }

    //for testing
    public Issue(Long id, Long readerId, Long bookId) {
        this.id = id;
        this.readerId = readerId;
        this.bookId = bookId;
        this.issueDate = LocalDateTime.now().withNano(0);
        this.returnedDate = null;
    }
}
