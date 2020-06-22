package com.skcc.book.domain;

public class BookCatalogEvent {

    private String title;

    private String description;

    private String author;

    private String publicationDate;

    private String classification;

    private Boolean rented;

    private String eventType;

    private Long rentCnt;


    public Long getRentCnt() {
        return rentCnt;
    }

    public void setRentCnt(Long rentCnt) {
        this.rentCnt = rentCnt;
    }

    public BookCatalogEvent(String title, String description, String author, String publicationDate, String classification, Boolean rented, String eventType, Long rentCnt) {
        this();
        this.title = title;
        this.description = description;
        this.author = author;
        this.publicationDate = publicationDate;
        this.classification = classification;
        this.rented = rented;
        this.eventType = eventType;
        this.rentCnt = rentCnt;
    }



    public BookCatalogEvent() {

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

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Boolean getRented() {
        return rented;
    }

    public void setRented(Boolean rented) {
        this.rented = rented;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
