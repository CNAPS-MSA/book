package com.skcc.book.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBookEvent {

    private Long bookId;
    private String bookStatus;


}
