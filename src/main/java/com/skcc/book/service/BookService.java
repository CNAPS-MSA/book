package com.skcc.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.book.domain.Book;

import com.skcc.book.web.rest.dto.BookInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service Interface for managing {@link com.skcc.book.domain.Book}.
 */
public interface BookService {

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    Book save(Book book);

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * Get the "id" book.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Book> findOne(Long id);

    /**
     * Delete the "id" book.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws InterruptedException, ExecutionException, JsonProcessingException;

    Book createBook(Book book) throws InterruptedException, ExecutionException, JsonProcessingException;

    Book updateBook(Book book) throws InterruptedException, ExecutionException, JsonProcessingException;

    void processChangeBookState(Long bookId, String bookStatus);


    Book findBookInfo(Long bookId);

    Book registerNewBook(Book book, Long inStockId) throws InterruptedException, ExecutionException, JsonProcessingException;

    void sendBookCatalogEvent(String eventType, Long bookId) throws InterruptedException, ExecutionException, JsonProcessingException;
}
