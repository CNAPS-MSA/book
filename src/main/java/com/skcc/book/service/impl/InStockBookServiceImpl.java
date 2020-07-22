package com.skcc.book.service.impl;

import com.skcc.book.service.InStockBookService;
import com.skcc.book.domain.InStockBook;
import com.skcc.book.repository.InStockBookRepository;
import com.skcc.book.web.rest.dto.InStockBookDTO;
import com.skcc.book.web.rest.mapper.InStockBookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InStockBook}.
 */
@Service
@Transactional
public class InStockBookServiceImpl implements InStockBookService {

    private final Logger log = LoggerFactory.getLogger(InStockBookServiceImpl.class);

    private final InStockBookRepository inStockBookRepository;

    private final InStockBookMapper inStockBookMapper;

    public InStockBookServiceImpl(InStockBookRepository inStockBookRepository, InStockBookMapper inStockBookMapper) {
        this.inStockBookRepository = inStockBookRepository;
        this.inStockBookMapper = inStockBookMapper;
    }

    /**
     * Save a inStockBook.
     *
     * @param inStockBook the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InStockBook save(InStockBook inStockBook) {
        log.debug("Request to save InStockBook : {}", inStockBook);
        return inStockBookRepository.save(inStockBook);
    }

    /**
     * Get all the inStockBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InStockBook> findAll(Pageable pageable) {
        log.debug("Request to get all InStockBooks");
        return inStockBookRepository.findAll(pageable);
    }

    /**
     * Get one inStockBook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public InStockBook findOne(Long id) {
        log.debug("Request to get InStockBook : {}", id);
        return inStockBookRepository.findById(id).get();
    }

    /**
     * Delete the inStockBook by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete InStockBook : {}", id);
        inStockBookRepository.deleteById(id);
    }

    @Override
    public Page<InStockBook> findByTitle(String title, Pageable pageable) {
        return inStockBookRepository.findByTitleLike(title, pageable);
    }
}
