package com.skcc.book.service;

import com.netflix.discovery.converters.Auto;
import com.skcc.book.adaptor.BookProducer;
import com.skcc.book.domain.Book;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.domain.enumeration.Classification;
import com.skcc.book.domain.enumeration.Location;
import com.skcc.book.repository.BookRepository;
import com.skcc.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = BookServiceImpl.class)
public class BookServiceTest {
    private static final Long BOOK_ID=1L;

    @Autowired
    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    BookProducer bookProducer;

    @MockBean
    InStockBookService inStockBookService;

    @MockBean
    EntityManager em;

    private Book book;

    public static Book createEntity(EntityManager em){
        Book book = new Book()
            .title("어린왕자")
            .location(Location.JEONGJA)
            .bookStatus(BookStatus.AVAILABLE)
            .author("ex_author")
            .classification(Classification.Children)
            .description("ex_어린왕자")
            .isbn(1L)
            .publisher("SK C&C")
            .publicationDate(LocalDate.now());
        em.persist(book);
        return book;
    }

    @BeforeEach
    public void initTest(){
        book = createEntity(em);
        book.setId(BOOK_ID);
        em.persist(book);
    }

    @Test
    void findBookInfoTest(){
        //given
        when(bookRepository.findById(BOOK_ID)).thenReturn(java.util.Optional.ofNullable(book));

        //when
        bookService.findBookInfo(BOOK_ID);

        //then
        verify(bookRepository).findById(BOOK_ID);
    }
}
