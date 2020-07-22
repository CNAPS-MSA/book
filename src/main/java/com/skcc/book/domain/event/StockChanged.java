
package com.skcc.book.domain.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockChanged {

    private Long bookId;
    private String bookStatus;


}
