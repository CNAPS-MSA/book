package com.skcc.book.web.rest.mapper;


import com.skcc.book.domain.*;
import com.skcc.book.web.rest.dto.InStockBookDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InStockBook} and its DTO {@link InStockBookDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InStockBookMapper extends EntityMapper<InStockBookDTO, InStockBook> {



    default InStockBook fromId(Long id) {
        if (id == null) {
            return null;
        }
        InStockBook inStockBook = new InStockBook();
        inStockBook.setId(id);
        return inStockBook;
    }
}
