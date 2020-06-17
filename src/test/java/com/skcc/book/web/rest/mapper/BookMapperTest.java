package com.skcc.book.web.rest.mapper;

import com.skcc.book.web.rest.mapper.BookMapperImpl;
import com.skcc.book.web.rest.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    public void setUp() {
        bookMapper = new BookMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bookMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bookMapper.fromId(null)).isNull();
    }
}
