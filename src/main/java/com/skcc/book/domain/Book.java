package com.skcc.book.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import com.skcc.book.domain.enumeration.Classification;

import com.skcc.book.domain.enumeration.BookStatus;

import com.skcc.book.domain.enumeration.Location;
import org.hibernate.annotations.Cascade;

/**
 * A Book.
 */

@Entity
@Table(name = "book")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@ToString
public class Book implements Serializable {

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
    @Column(name = "classification")
    private Classification classification;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_status")
    private BookStatus bookStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public Book description(String description) {
        this.description = description;
        return this;
    }


    public Book author(String author) {
        this.author = author;
        return this;
    }


    public Book publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }


    public Book isbn(Long isbn) {
        this.isbn = isbn;
        return this;
    }


    public Book publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }


    public Book classification(Classification classification) {
        this.classification = classification;
        return this;
    }


    public Book bookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
        return this;
    }


    public Book location(Location location) {
        this.location = location;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }




}
