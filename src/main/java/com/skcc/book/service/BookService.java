package com.skcc.book.service;

import com.skcc.book.domain.Book;
import com.skcc.book.web.rest.dto.BookDTO;

import com.skcc.book.web.rest.dto.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
    void delete(Long id);

    List<BookInfo> getBookInfo(List<Long> bookIds, Long userId);

    Book makeReservation(Book book, Long userId, Long bookResCnt);

    Book getBooks(Long bookId);
}
