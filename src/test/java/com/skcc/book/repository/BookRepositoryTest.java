package com.skcc.book.repository;

import com.skcc.book.domain.Book;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.domain.enumeration.Classification;
import com.skcc.book.domain.enumeration.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class BookRepositoryTest{

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void create(){

        Book book = new Book();
        book.setId((long)1);
        book.setPublisher("pub");
        book.setAuthor("author");
        book.setBookStatus(BookStatus.AVAILABLE);
        book.setClassification(Classification.Biographies);
        book.setLocation(Location.JEONGJA);
        book.setDescription("desc");
        book.setIsbn((long)124);
        book.setPublicationDate(LocalDate.now());
        book.setTitle("title");

        book=bookRepository.save(book);
        System.out.println(book);

    }

    @Test
    public void updateBook(){
        Book book = bookRepository.findById((long)1).get();

        book = bookRepository.save(book);

    }

}
