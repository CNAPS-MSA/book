package com.skcc.book.web.rest.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.skcc.book.domain.enumeration.Source;

/**
 * A DTO for the {@link com.skcc.book.domain.InStockBook} entity.
 */
public class InStockBookDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String author;

    private String publisher;

    private Long isbn;

    private LocalDate publicationDate;

    private Source source;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InStockBookDTO inStockBookDTO = (InStockBookDTO) o;
        if (inStockBookDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inStockBookDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InStockBookDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", author='" + getAuthor() + "'" +
            ", publisher='" + getPublisher() + "'" +
            ", isbn=" + getIsbn() +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", source='" + getSource() + "'" +
            "}";
    }
}
