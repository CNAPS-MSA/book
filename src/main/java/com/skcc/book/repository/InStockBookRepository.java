package com.skcc.book.repository;

import com.skcc.book.domain.InStockBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InStockBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InStockBookRepository extends JpaRepository<InStockBook, Long> {
    Page<InStockBook> findByTitleContaining(String title, Pageable pageable);
}
