package com.skcc.book.service;

import com.skcc.book.domain.InStockBook;
import com.skcc.book.web.rest.dto.InStockBookDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.skcc.book.domain.InStockBook}.
 */
public interface InStockBookService {

    /**
     * Save a inStockBook.
     *
     * @param inStockBook the entity to save.
     * @return the persisted entity.
     */
    InStockBook save(InStockBook inStockBook);

    /**
     * Get all the inStockBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InStockBook> findAll(Pageable pageable);

    /**
     * Get the "id" inStockBook.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    InStockBook findOne(Long id);

    /**
     * Delete the "id" inStockBook.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<InStockBook> findByTitle(String title, Pageable pageable);
}
