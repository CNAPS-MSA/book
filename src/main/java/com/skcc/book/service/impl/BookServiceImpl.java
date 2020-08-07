package com.skcc.book.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.book.adaptor.BookProducer;
import com.skcc.book.domain.event.CatalogChanged;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.service.BookService;
import com.skcc.book.domain.Book;
import com.skcc.book.repository.BookRepository;
import com.skcc.book.web.rest.dto.BookInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    private final BookProducer bookProducer;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BookServiceImpl(BookRepository bookRepository, BookProducer bookProducer) {
        this.bookRepository = bookRepository;
        this.bookProducer = bookProducer;
    }

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);
        return bookRepository.save(book);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findById(id);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookInfoDTO findBookInfo(Long bookId) {
        BookInfoDTO bookInfoDTO = new BookInfoDTO();
        Book book = bookRepository.findById(bookId).get();
        bookInfoDTO.setId(book.getId());
        bookInfoDTO.setTitle(bookRepository.findById(book.getId()).get().getTitle());
        return bookInfoDTO;
    }


    @Override
    public void sendBookCatalogEvent(String eventType,Long bookId) throws InterruptedException, ExecutionException, JsonProcessingException {
        Book book = bookRepository.findById(bookId).get();
        CatalogChanged catalogChanged = new CatalogChanged();
        if(eventType.equals("NEW_BOOK") || eventType.equals("UPDATE_BOOK")) {
            catalogChanged.setBookId(book.getId());
            catalogChanged.setAuthor(book.getAuthor());
            catalogChanged.setClassification(book.getClassification().toString());
            catalogChanged.setDescription(book.getDescription());
            catalogChanged.setPublicationDate(book.getPublicationDate().format(fmt));
            catalogChanged.setTitle(book.getTitle());
            catalogChanged.setEventType(eventType);
            catalogChanged.setRented(!book.getBookStatus().equals(BookStatus.AVAILABLE));
            catalogChanged.setRentCnt((long) 0);
            bookProducer.sendBookCreateEvent(catalogChanged);
        }else if(eventType.equals("DELETE_BOOK")){
            catalogChanged.setEventType(eventType);
            catalogChanged.setBookId(book.getId());
            bookProducer.sendBookDeleteEvent(catalogChanged);
        }
    }


}
