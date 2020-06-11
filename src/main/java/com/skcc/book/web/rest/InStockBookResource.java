package com.skcc.book.web.rest;

import com.skcc.book.service.InStockBookService;
import com.skcc.book.web.rest.errors.BadRequestAlertException;
import com.skcc.book.web.rest.dto.InStockBookDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.skcc.book.domain.InStockBook}.
 */
@RestController
@RequestMapping("/api")
public class InStockBookResource {

    private final Logger log = LoggerFactory.getLogger(InStockBookResource.class);

    private static final String ENTITY_NAME = "bookInStockBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InStockBookService inStockBookService;

    public InStockBookResource(InStockBookService inStockBookService) {
        this.inStockBookService = inStockBookService;
    }

    /**
     * {@code POST  /in-stock-books} : Create a new inStockBook.
     *
     * @param inStockBookDTO the inStockBookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inStockBookDTO, or with status {@code 400 (Bad Request)} if the inStockBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/in-stock-books")
    public ResponseEntity<InStockBookDTO> createInStockBook(@RequestBody InStockBookDTO inStockBookDTO) throws URISyntaxException {
        log.debug("REST request to save InStockBook : {}", inStockBookDTO);
        if (inStockBookDTO.getId() != null) {
            throw new BadRequestAlertException("A new inStockBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InStockBookDTO result = inStockBookService.save(inStockBookDTO);
        return ResponseEntity.created(new URI("/api/in-stock-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /in-stock-books} : Updates an existing inStockBook.
     *
     * @param inStockBookDTO the inStockBookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inStockBookDTO,
     * or with status {@code 400 (Bad Request)} if the inStockBookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inStockBookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/in-stock-books")
    public ResponseEntity<InStockBookDTO> updateInStockBook(@RequestBody InStockBookDTO inStockBookDTO) throws URISyntaxException {
        log.debug("REST request to update InStockBook : {}", inStockBookDTO);
        if (inStockBookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InStockBookDTO result = inStockBookService.save(inStockBookDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inStockBookDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /in-stock-books} : get all the inStockBooks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inStockBooks in body.
     */
    @GetMapping("/in-stock-books")
    public ResponseEntity<List<InStockBookDTO>> getAllInStockBooks(Pageable pageable) {
        log.debug("REST request to get a page of InStockBooks");
        Page<InStockBookDTO> page = inStockBookService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /in-stock-books/:id} : get the "id" inStockBook.
     *
     * @param id the id of the inStockBookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inStockBookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/in-stock-books/{id}")
    public ResponseEntity<InStockBookDTO> getInStockBook(@PathVariable Long id) {
        log.debug("REST request to get InStockBook : {}", id);
        Optional<InStockBookDTO> inStockBookDTO = inStockBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inStockBookDTO);
    }

    /**
     * {@code DELETE  /in-stock-books/:id} : delete the "id" inStockBook.
     *
     * @param id the id of the inStockBookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/in-stock-books/{id}")
    public ResponseEntity<Void> deleteInStockBook(@PathVariable Long id) {
        log.debug("REST request to delete InStockBook : {}", id);
        inStockBookService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
