package com.skcc.book.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.book.web.rest.TestUtil;

public class InStockBookTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InStockBook.class);
        InStockBook inStockBook1 = new InStockBook();
        inStockBook1.setId(1L);
        InStockBook inStockBook2 = new InStockBook();
        inStockBook2.setId(inStockBook1.getId());
        assertThat(inStockBook1).isEqualTo(inStockBook2);
        inStockBook2.setId(2L);
        assertThat(inStockBook1).isNotEqualTo(inStockBook2);
        inStockBook1.setId(null);
        assertThat(inStockBook1).isNotEqualTo(inStockBook2);
    }
}
