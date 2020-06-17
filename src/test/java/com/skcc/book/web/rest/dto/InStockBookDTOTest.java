package com.skcc.book.web.rest.dto;

import com.skcc.book.web.rest.dto.InStockBookDTO;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.skcc.book.web.rest.TestUtil;

public class InStockBookDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InStockBookDTO.class);
        InStockBookDTO inStockBookDTO1 = new InStockBookDTO();
        inStockBookDTO1.setId(1L);
        InStockBookDTO inStockBookDTO2 = new InStockBookDTO();
        assertThat(inStockBookDTO1).isNotEqualTo(inStockBookDTO2);
        inStockBookDTO2.setId(inStockBookDTO1.getId());
        assertThat(inStockBookDTO1).isEqualTo(inStockBookDTO2);
        inStockBookDTO2.setId(2L);
        assertThat(inStockBookDTO1).isNotEqualTo(inStockBookDTO2);
        inStockBookDTO1.setId(null);
        assertThat(inStockBookDTO1).isNotEqualTo(inStockBookDTO2);
    }
}
