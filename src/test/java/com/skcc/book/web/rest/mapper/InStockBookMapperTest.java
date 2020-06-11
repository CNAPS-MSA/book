package com.skcc.book.web.rest.mapper;

import com.skcc.book.web.rest.mapper.InStockBookMapperImpl;
import com.skcc.book.web.rest.mapper.InStockBookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InStockBookMapperTest {

    private InStockBookMapper inStockBookMapper;

    @BeforeEach
    public void setUp() {
        inStockBookMapper = new InStockBookMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(inStockBookMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(inStockBookMapper.fromId(null)).isNull();
    }
}
