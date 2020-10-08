package com.skcc.book.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.book.adaptor.BookProducer;
import com.skcc.book.adaptor.BookProducerService;
import com.skcc.book.domain.event.BookChanged;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.service.BookService;
import com.skcc.book.domain.Book;
import com.skcc.book.repository.BookRepository;
import com.skcc.book.service.InStockBookService;
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

    private final BookProducerService bookProducerService;

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final InStockBookService inStockBookService;
    public BookServiceImpl(BookRepository bookRepository, BookProducerService bookProducerService, InStockBookService inStockBookService) {
        this.bookRepository = bookRepository;
        this.bookProducerService = bookProducerService;
        this.inStockBookService = inStockBookService;
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
    public void delete(Long id) throws InterruptedException, ExecutionException, JsonProcessingException {
        log.debug("Request to delete Book : {}", id);
        sendBookCatalogEvent("DELETE_BOOK", id);
        bookRepository.deleteById(id);
    }

    @Override
    public Book createBook(Book book) throws InterruptedException, ExecutionException, JsonProcessingException {
        Book createdBook = bookRepository.save(book);
        sendBookCatalogEvent("NEW_BOOK",createdBook.getId());
        return createdBook;
    }

    @Override
    public Book updateBook(Book book) throws InterruptedException, ExecutionException, JsonProcessingException {
        Book updatedBook = bookRepository.save(book);
        sendBookCatalogEvent("UPDATE_BOOK",book.getId());
        return updatedBook;
    }

    @Override
    public void processChangeBookState(Long bookId, String bookStatus) {
        Book book = bookRepository.findById(bookId).get();
        book.setBookStatus(BookStatus.valueOf(bookStatus));
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book findBookInfo(Long bookId) {
       return bookRepository.findById(bookId).get();

    }

    @Override
    public Book registerNewBook(Book book, Long inStockId) throws InterruptedException, ExecutionException, JsonProcessingException {
        Book newBook =bookRepository.save(book);
        inStockBookService.delete(inStockId);
        sendBookCatalogEvent("NEW_BOOK",newBook.getId()); //send kafka - bookcatalog
        return newBook;
    }


    @Override
    public void sendBookCatalogEvent(String eventType,Long bookId) throws InterruptedException, ExecutionException, JsonProcessingException {
        Book book = bookRepository.findById(bookId).get();
        BookChanged bookChanged = new BookChanged();
        if(eventType.equals("NEW_BOOK") || eventType.equals("UPDATE_BOOK")) {
            bookChanged.setBookId(book.getId());
            bookChanged.setAuthor(book.getAuthor());
            bookChanged.setClassification(book.getClassification().toString());
            bookChanged.setDescription(book.getDescription());
            bookChanged.setPublicationDate(book.getPublicationDate().format(fmt));
            bookChanged.setTitle(book.getTitle());
            bookChanged.setEventType(eventType);
            bookChanged.setRented(!book.getBookStatus().equals(BookStatus.AVAILABLE));
            bookChanged.setRentCnt((long) 0);
            bookProducerService.sendBookCreateEvent(bookChanged);
        }else if(eventType.equals("DELETE_BOOK")){
            bookChanged.setEventType(eventType);
            bookChanged.setBookId(book.getId());
            bookProducerService.sendBookDeleteEvent(bookChanged);
        }
    }


}
