package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
@Data
@NoArgsConstructor
public class IssueEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "readerId")
    private long readerId;

    @Column(name = "bookId")
    private long bookId;
    @Column(name = "issueDate")
    private LocalDateTime issueDate;
    @Column(name = "returnedDate")
    private LocalDateTime returnedDate;


    public IssueEntity(long readerId, long bookId) {
        this.readerId = readerId;
        this.bookId = bookId;

        this.issueDate = LocalDateTime.now().withNano(0);
        this.returnedDate = null;
    }
}
