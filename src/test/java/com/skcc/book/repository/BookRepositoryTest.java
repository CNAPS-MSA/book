package com.skcc.book.repository;

import com.skcc.book.domain.Book;
import com.skcc.book.domain.BookReservation;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.domain.enumeration.Classification;
import com.skcc.book.domain.enumeration.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
        BookReservation bookReservation = new BookReservation((long)1, (long)2);

        BookReservation bookReservation2 = new BookReservation((long)2, (long)3);
        book.addBookReservation(bookReservation);
        book.addBookReservation(bookReservation2);
        book.setTitle("title");

        book=bookRepository.save(book);
        System.out.println(book);

    }

    @Test
    public void updateBook(){
        Book book = bookRepository.findById((long)1).get();
        book.addBookReservation(new BookReservation((long)1, (long)2));
        book.removeBookReservationByUserId((long)2);
        book = bookRepository.save(book);
        book.getbookReservations().forEach(b-> System.out.println("userId: "+ b.getUserId() + "seq"+b.getReservedSeqNo()));
    }

}
