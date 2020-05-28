package com.skcc.book.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BookInfo implements Serializable {
    private Long id;

    private String title;

    public BookInfo(Long id, String title) {
        this.id = id;
        this.title = title;
    }

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
}
