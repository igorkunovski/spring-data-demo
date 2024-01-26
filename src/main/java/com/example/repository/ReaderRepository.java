package com.example.repository;

import com.example.entity.ReaderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReaderRepository extends JpaRepository<ReaderEntity, Long> {

}