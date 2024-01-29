package com.example.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.reader")
@Data
public class ReaderProperties {
    private int maxAllowedBooks;
}
