package com.skcc.book.service.impl;

import com.skcc.book.domain.BookReservation;
import com.skcc.book.domain.enumeration.BookStatus;
import com.skcc.book.service.BookService;
import com.skcc.book.domain.Book;
import com.skcc.book.repository.BookRepository;
import com.skcc.book.web.rest.dto.BookInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;


    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);
        return bookRepository.save(book);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findById(id);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookInfo> getBookInfo(List<Long> bookIds, Long userId) {
        List<BookInfo> bookInfoList = new ArrayList<>();
        for(Long bookId: bookIds){
            Book book = bookRepository.findById(bookId).get();
                if(book.getBookStatus().equals(BookStatus.AVAILABLE)){ //대여가능상태
                    if(book.getbookReservations().size()==0) { //예약자 없으면
                        bookInfoList.add(new BookInfo(bookId, bookRepository.findById(bookId).get().getTitle()));
                    }else{
                        if(book.checkReservationContains(userId)){ //예약자 리스트에 있으면
                            if(book.isFirstReservation(userId)){ //예약자 1번인 경우
                                bookInfoList.add(new BookInfo(bookId, bookRepository.findById(bookId).get().getTitle())); //예약
                                book=book.removeBookReservationByUserId(userId); // 예약자리스트에서 삭제
                                bookRepository.save(book);
                            }
                        }
                    }
                }
                else{ //대여 불가능 상태
                    if (!book.checkReservationContains(userId)) { //예약자 리스트에 없으면
                        log.debug("check Reservation", false);
                       book= makeReservation(book, userId, (long) book.getbookReservations().size()); //리스트에 추가
                    }
                    System.out.println("book Reservation Size:"+ book.getbookReservations().size());
                    //log.debug("book Reservation Size:", book.getbookReservations().size());
                }


        }
        return bookInfoList;
    }
    //책의 예약자 목록에 추가
    @Override
    public Book makeReservation(Book book, Long userId, Long bookResCnt) {

        book=book.addBookReservation(new BookReservation(userId, bookResCnt+1));
        System.out.println("Make Reservation: ");
        book.getbookReservations().forEach(b-> System.out.println(b.getUserId()+"&&"+b.getReservedSeqNo()));
        book=bookRepository.save(book);
        return book;
    }




}
