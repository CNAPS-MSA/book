package com.skcc.book.domain;

import com.skcc.book.domain.converter.BookReservationConverter;
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


    @Convert(converter = BookReservationConverter.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Column(name="book_reservation")
    private Set<BookReservation> bookReservations= new HashSet<>();

    public Set<BookReservation> getbookReservations() {
        return bookReservations;
    }

    public Book bookReservations(Set<BookReservation> bookReservations) {
        this.bookReservations = bookReservations;
        return this;
    }

    public Book addBookReservation(BookReservation bookReservation) {
        this.bookReservations.add(bookReservation);
        return this;
    }

    public Book removeBookReservation(BookReservation bookReservation) {
        this.bookReservations.remove(bookReservation);
        return this;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Book description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public Book publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getIsbn() {
        return isbn;
    }

    public Book isbn(Long isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public Book publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Classification getClassification() {
        return classification;
    }

    public Book classification(Classification classification) {
        this.classification = classification;
        return this;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public BookStatus getBookStatus() {
        return bookStatus;
    }

    public Book bookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
        return this;
    }

    public void setBookStatus(BookStatus bookStatus) {
        this.bookStatus = bookStatus;
    }

    public Location getLocation() {
        return location;
    }

    public Book location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", author='" + getAuthor() + "'" +
            ", publisher='" + getPublisher() + "'" +
            ", isbn=" + getIsbn() +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", classification='" + getClassification() + "'" +
            ", bookStatus='" + getBookStatus() + "'" +
            ", location='" + getLocation() + "'" +
            ", bookReservations=" + getbookReservations()+"'"+
            "}";
    }

    public boolean checkReservationContains(Long userId){
        return this.getbookReservations().stream().allMatch(b -> b.getUserId().equals(userId));
    }

    public boolean isFirstReservation(Long userId){
        return Objects.equals(this.getbookReservations().stream()
            .sorted(Comparator.comparing(BookReservation::getReservedSeqNo)).findFirst().get().getUserId(), userId);
    }

    public Book removeBookReservationByUserId(Long userId){
        BookReservation bookReservation = this.getbookReservations().stream().filter(b -> b.getUserId().equals(userId)).findAny().get();
        return this.removeBookReservation(bookReservation);

    }

}
