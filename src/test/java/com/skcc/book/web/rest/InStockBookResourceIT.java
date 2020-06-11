package com.skcc.book.web.rest;

import com.skcc.book.BookApp;
import com.skcc.book.domain.InStockBook;
import com.skcc.book.repository.InStockBookRepository;
import com.skcc.book.service.InStockBookService;
import com.skcc.book.web.rest.dto.InStockBookDTO;
import com.skcc.book.web.rest.mapper.InStockBookMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skcc.book.domain.enumeration.Source;
/**
 * Integration tests for the {@link InStockBookResource} REST controller.
 */
@SpringBootTest(classes = BookApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class InStockBookResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    private static final Long DEFAULT_ISBN = 1L;
    private static final Long UPDATED_ISBN = 2L;

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Source DEFAULT_SOURCE = Source.Donated;
    private static final Source UPDATED_SOURCE = Source.Purchased;

    @Autowired
    private InStockBookRepository inStockBookRepository;

    @Autowired
    private InStockBookMapper inStockBookMapper;

    @Autowired
    private InStockBookService inStockBookService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInStockBookMockMvc;

    private InStockBook inStockBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InStockBook createEntity(EntityManager em) {
        InStockBook inStockBook = new InStockBook()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .author(DEFAULT_AUTHOR)
            .publisher(DEFAULT_PUBLISHER)
            .isbn(DEFAULT_ISBN)
            .publicationDate(DEFAULT_PUBLICATION_DATE)
            .source(DEFAULT_SOURCE);
        return inStockBook;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InStockBook createUpdatedEntity(EntityManager em) {
        InStockBook inStockBook = new InStockBook()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER)
            .isbn(UPDATED_ISBN)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .source(UPDATED_SOURCE);
        return inStockBook;
    }

    @BeforeEach
    public void initTest() {
        inStockBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createInStockBook() throws Exception {
        int databaseSizeBeforeCreate = inStockBookRepository.findAll().size();

        // Create the InStockBook
        InStockBookDTO inStockBookDTO = inStockBookMapper.toDto(inStockBook);
        restInStockBookMockMvc.perform(post("/api/in-stock-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inStockBookDTO)))
            .andExpect(status().isCreated());

        // Validate the InStockBook in the database
        List<InStockBook> inStockBookList = inStockBookRepository.findAll();
        assertThat(inStockBookList).hasSize(databaseSizeBeforeCreate + 1);
        InStockBook testInStockBook = inStockBookList.get(inStockBookList.size() - 1);
        assertThat(testInStockBook.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testInStockBook.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInStockBook.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testInStockBook.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(testInStockBook.getIsbn()).isEqualTo(DEFAULT_ISBN);
        assertThat(testInStockBook.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testInStockBook.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void createInStockBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inStockBookRepository.findAll().size();

        // Create the InStockBook with an existing ID
        inStockBook.setId(1L);
        InStockBookDTO inStockBookDTO = inStockBookMapper.toDto(inStockBook);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInStockBookMockMvc.perform(post("/api/in-stock-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inStockBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InStockBook in the database
        List<InStockBook> inStockBookList = inStockBookRepository.findAll();
        assertThat(inStockBookList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInStockBooks() throws Exception {
        // Initialize the database
        inStockBookRepository.saveAndFlush(inStockBook);

        // Get all the inStockBookList
        restInStockBookMockMvc.perform(get("/api/in-stock-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inStockBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)))
            .andExpect(jsonPath("$.[*].isbn").value(hasItem(DEFAULT_ISBN.intValue())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    public void getInStockBook() throws Exception {
        // Initialize the database
        inStockBookRepository.saveAndFlush(inStockBook);

        // Get the inStockBook
        restInStockBookMockMvc.perform(get("/api/in-stock-books/{id}", inStockBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inStockBook.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER))
            .andExpect(jsonPath("$.isbn").value(DEFAULT_ISBN.intValue()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInStockBook() throws Exception {
        // Get the inStockBook
        restInStockBookMockMvc.perform(get("/api/in-stock-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInStockBook() throws Exception {
        // Initialize the database
        inStockBookRepository.saveAndFlush(inStockBook);

        int databaseSizeBeforeUpdate = inStockBookRepository.findAll().size();

        // Update the inStockBook
        InStockBook updatedInStockBook = inStockBookRepository.findById(inStockBook.getId()).get();
        // Disconnect from session so that the updates on updatedInStockBook are not directly saved in db
        em.detach(updatedInStockBook);
        updatedInStockBook
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .author(UPDATED_AUTHOR)
            .publisher(UPDATED_PUBLISHER)
            .isbn(UPDATED_ISBN)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .source(UPDATED_SOURCE);
        InStockBookDTO inStockBookDTO = inStockBookMapper.toDto(updatedInStockBook);

        restInStockBookMockMvc.perform(put("/api/in-stock-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inStockBookDTO)))
            .andExpect(status().isOk());

        // Validate the InStockBook in the database
        List<InStockBook> inStockBookList = inStockBookRepository.findAll();
        assertThat(inStockBookList).hasSize(databaseSizeBeforeUpdate);
        InStockBook testInStockBook = inStockBookList.get(inStockBookList.size() - 1);
        assertThat(testInStockBook.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testInStockBook.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInStockBook.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testInStockBook.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
        assertThat(testInStockBook.getIsbn()).isEqualTo(UPDATED_ISBN);
        assertThat(testInStockBook.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testInStockBook.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void updateNonExistingInStockBook() throws Exception {
        int databaseSizeBeforeUpdate = inStockBookRepository.findAll().size();

        // Create the InStockBook
        InStockBookDTO inStockBookDTO = inStockBookMapper.toDto(inStockBook);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInStockBookMockMvc.perform(put("/api/in-stock-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inStockBookDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InStockBook in the database
        List<InStockBook> inStockBookList = inStockBookRepository.findAll();
        assertThat(inStockBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInStockBook() throws Exception {
        // Initialize the database
        inStockBookRepository.saveAndFlush(inStockBook);

        int databaseSizeBeforeDelete = inStockBookRepository.findAll().size();

        // Delete the inStockBook
        restInStockBookMockMvc.perform(delete("/api/in-stock-books/{id}", inStockBook.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InStockBook> inStockBookList = inStockBookRepository.findAll();
        assertThat(inStockBookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
