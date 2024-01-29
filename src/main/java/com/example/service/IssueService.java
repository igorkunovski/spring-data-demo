package com.example.service;

import com.example.entity.Issue;
import com.example.entity.IssueRequest;

import java.util.List;
import java.util.Optional;

public interface IssueService {
    List<Issue> getOpenedIssues();
    List<Issue> findAll();
    void addNewOpenedIssue(Issue issue);
    List<Issue> getUserIssues(long id);
    Issue issue(IssueRequest request);
    void closeIssue(Issue issue);
    Optional<Issue> findById(Long id);
}
