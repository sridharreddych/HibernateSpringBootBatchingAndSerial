package com.bookstore;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.bookstore.service.BookstoreService;

@SpringBootApplication
public class MainApplication {

    private final BookstoreService bookstoreService;

    public MainApplication(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            bookstoreService.authorBatchInserts();
        };
    }
}


/*
Why To Avoid PostgreSQL (BIG)SERIAL In Batching Inserts Via Hibernate

Description: In PostgreSQL, using GenerationType.IDENTITY will disable insert batching. The (BIG)SERIAL is acting "almost" like MySQL, AUTO_INCREMENT. In this application, we use the GenerationType.SEQUENCE which permits insert batching, and we optimize it via the hi/lo optimization algorithm.

Key points:

use GenerationType.SEQUENCE instead of GenerationType.IDENTITY
rely on the hi/lo algorithm to fetch a hi value in a database roundtrip (the hi value is useful for generating a certain/given number of identifiers in-memory; until you haven't exhausted all in-memory identifiers there is no need to fetch another hi)
you can go even further and use the Hibernate pooled and pooled-lo identifier generators (these are optimizations of hi/lo that allows external services to use the database without causing duplication keys errors)
*/