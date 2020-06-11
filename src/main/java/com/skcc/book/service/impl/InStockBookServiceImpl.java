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
     * @param inStockBookDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public InStockBookDTO save(InStockBookDTO inStockBookDTO) {
        log.debug("Request to save InStockBook : {}", inStockBookDTO);
        InStockBook inStockBook = inStockBookMapper.toEntity(inStockBookDTO);
        inStockBook = inStockBookRepository.save(inStockBook);
        return inStockBookMapper.toDto(inStockBook);
    }

    /**
     * Get all the inStockBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InStockBookDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InStockBooks");
        return inStockBookRepository.findAll(pageable)
            .map(inStockBookMapper::toDto);
    }

    /**
     * Get one inStockBook by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<InStockBookDTO> findOne(Long id) {
        log.debug("Request to get InStockBook : {}", id);
        return inStockBookRepository.findById(id)
            .map(inStockBookMapper::toDto);
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
}
