package com.skcc.book.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCatalogEvent {

    private String title;

    private String description;

    private String author;

    private String publicationDate;

    private String classification;

    private Boolean rented;

    private String eventType;

    private Long rentCnt;

    private Long bookId;

   
}
