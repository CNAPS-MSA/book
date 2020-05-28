package com.skcc.book.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.domain.enumeration.Categories;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.skcc.book.domain.Book} entity. This class is used
 * in {@link com.skcc.book.web.rest.BookResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /books?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookCriteria implements Serializable, Criteria {
    /**
     * Class for filtering BookStatus
     */
    public static class BookStatusFilter extends Filter<BookStatus> {

        public BookStatusFilter() {
        }

        public BookStatusFilter(BookStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookStatusFilter copy() {
            return new BookStatusFilter(this);
        }

    }
    /**
     * Class for filtering Categories
     */
    public static class CategoriesFilter extends Filter<Categories> {

        public CategoriesFilter() {
        }

        public CategoriesFilter(CategoriesFilter filter) {
            super(filter);
        }

        @Override
        public CategoriesFilter copy() {
            return new CategoriesFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter author;

    private StringFilter description;

    private BookStatusFilter bookStatus;

    private CategoriesFilter category;

    private IntegerFilter barcode;

    public BookCriteria() {
    }

    public BookCriteria(BookCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.author = other.author == null ? null : other.author.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.bookStatus = other.bookStatus == null ? null : other.bookStatus.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.barcode = other.barcode == null ? null : other.barcode.copy();
    }

    @Override
    public BookCriteria copy() {
        return new BookCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BookStatusFilter getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(BookStatusFilter bookStatus) {
        this.bookStatus = bookStatus;
    }

    public CategoriesFilter getCategory() {
        return category;
    }

    public void setCategory(CategoriesFilter category) {
        this.category = category;
    }

    public IntegerFilter getBarcode() {
        return barcode;
    }

    public void setBarcode(IntegerFilter barcode) {
        this.barcode = barcode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookCriteria that = (BookCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(author, that.author) &&
            Objects.equals(description, that.description) &&
            Objects.equals(bookStatus, that.bookStatus) &&
            Objects.equals(category, that.category) &&
            Objects.equals(barcode, that.barcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        author,
        description,
        bookStatus,
        category,
        barcode
        );
    }

    @Override
    public String toString() {
        return "BookCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (author != null ? "author=" + author + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (bookStatus != null ? "bookStatus=" + bookStatus + ", " : "") +
                (category != null ? "category=" + category + ", " : "") +
                (barcode != null ? "barcode=" + barcode + ", " : "") +
            "}";
    }

}
