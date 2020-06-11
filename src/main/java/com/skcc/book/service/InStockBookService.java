package com.skcc.book.service;

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
     * @param inStockBookDTO the entity to save.
     * @return the persisted entity.
     */
    InStockBookDTO save(InStockBookDTO inStockBookDTO);

    /**
     * Get all the inStockBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InStockBookDTO> findAll(Pageable pageable);

    /**
     * Get the "id" inStockBook.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InStockBookDTO> findOne(Long id);

    /**
     * Delete the "id" inStockBook.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
