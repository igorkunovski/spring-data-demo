package com.example.service;

import com.example.entity.IssueEntity;
import com.example.entity.IssueRequestEntity;

import java.util.List;

public interface IssueService {
    List<IssueEntity> getOpenedIssues();
    List<IssueEntity> getAllIssues();
    void addNewOpenedIssue(IssueEntity issue);
    List<IssueEntity> getUserIssues(long id);
    IssueEntity issue(IssueRequestEntity request);
    IssueEntity getIssueById(long id);
    void closeIssue(IssueEntity issue);
}
