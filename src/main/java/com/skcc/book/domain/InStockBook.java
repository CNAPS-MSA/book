package com.skcc.book.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import com.skcc.book.domain.enumeration.Source;

/**
 * A InStockBook.
 */
@Entity
@Table(name = "in_stock_book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@ToString
public class InStockBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "isbn")
    private Long isbn;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private Source source;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public InStockBook title(String title) {
        this.title = title;
        return this;
    }


    public InStockBook description(String description) {
        this.description = description;
        return this;
    }


    public InStockBook author(String author) {
        this.author = author;
        return this;
    }


    public InStockBook publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }


    public InStockBook isbn(Long isbn) {
        this.isbn = isbn;
        return this;
    }


    public InStockBook publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }


    public InStockBook source(Source source) {
        this.source = source;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InStockBook)) {
            return false;
        }
        return id != null && id.equals(((InStockBook) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }


}
