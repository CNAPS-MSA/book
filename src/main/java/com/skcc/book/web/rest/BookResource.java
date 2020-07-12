package com.skcc.book.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.book.domain.Book;
import com.skcc.book.service.BookService;
import com.skcc.book.service.InStockBookService;
import com.skcc.book.web.rest.dto.BookInfoDTO;
import com.skcc.book.web.rest.errors.BadRequestAlertException;
import com.skcc.book.web.rest.dto.BookDTO;

import com.skcc.book.web.rest.mapper.BookMapper;
import com.skcc.book.web.rest.mapper.InStockBookMapper;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * REST controller for managing {@link com.skcc.book.domain.Book}.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "bookBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookService bookService;
    private final InStockBookService inStockBookService;
    private final InStockBookMapper inStockBookMapper;
    private final BookMapper bookMapper;

    public BookResource(BookService bookService, InStockBookService inStockBookService, InStockBookMapper inStockBookMapper, BookMapper bookMapper) {
        this.bookService = bookService;
        this.inStockBookService = inStockBookService;
        this.inStockBookMapper = inStockBookMapper;
        this.bookMapper = bookMapper;
    }

    /**
     * {@code POST  /books} : Create a new book.
     *
     * @param bookDTO the bookDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookDTO, or with status {@code 400 (Bad Request)} if the book has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to save Book : {}", bookDTO);
        if (bookDTO.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Book newBook = bookService.save(bookMapper.toEntity(bookDTO));
        try {
            bookService.sendBookCatalogEvent("NEW_BOOK",newBook.getId()); //send kafka - bookcatalog
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            e.printStackTrace();
        }
        BookDTO result = bookMapper.toDto(newBook);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /books} : Updates an existing book.
     *
     * @param bookDTO the bookDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookDTO,
     * or with status {@code 400 (Bad Request)} if the bookDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books")
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO) throws URISyntaxException {
        log.debug("REST request to update Book : {}", bookDTO);
        if (bookDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Book book = bookService.save(bookMapper.toEntity(bookDTO));
        try {
            bookService.sendBookCatalogEvent("UPDATE_BOOK",book.getId()); //send kafka - bookcatalog
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            e.printStackTrace();
        }
        BookDTO result = bookMapper.toDto(book);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of books in body.
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBooks(Pageable pageable) {
        log.debug("REST request to get a page of Books");
        Page<Book> page = bookService.findAll(pageable);
        List<BookDTO> bookDTOS = bookMapper.toDto(page.getContent());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), new PageImpl<>(bookDTOS));
        return ResponseEntity.ok().headers(headers).body(bookDTOS);
    }

    /**
     * {@code GET  /books/:id} : get the "id" book.
     *
     * @param id the id of the bookDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        BookDTO bookDTO = bookMapper.toDto(bookService.findOne(id).get());
        return ResponseEntity.ok().body(bookDTO);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" book.
     *
     * @param id the id of the bookDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        try {
            bookService.sendBookCatalogEvent("DELETE_BOOK", id);
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            e.printStackTrace();
        }
        bookService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/getBookInfo/{bookIds}/{userid}")
    public ResponseEntity<List<BookInfoDTO>> getBookInfo(@PathVariable("bookIds") List<Long> bookIds, @PathVariable("userid")Long userid){
        log.debug("Got feign request!!");
        List<BookInfoDTO> bookInfoDTOList = bookService.getBookInfo(bookIds, userid);
        log.debug(bookInfoDTOList.toString());
        return ResponseEntity.ok().body(bookInfoDTOList);
    }

    @GetMapping("/getBook/{bookId}")
    public ResponseEntity<Book> getBooks(@PathVariable("bookId")Long bookId){
        Book book = bookService.getBooks(bookId);
        return ResponseEntity.ok().body(book);
    }
    /********
     *
     * Register in stock books
     *
     * ******/


    /****
     *
     *
     * Register a new book from in stock book
     *
     *
     * ***/







}
