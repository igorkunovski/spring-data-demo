package com.example.repository;

import com.example.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {

}
